package com.fiapx.core.domain.services.ProcessVideoUseCase;

import com.fiapx.core.application.exceptions.DirectoryManagerException;
import com.fiapx.core.domain.entities.EProcessingStatus;
import com.fiapx.core.domain.entities.ProcessingRequest;
import com.fiapx.core.domain.repositories.IProcessingRequestRepository;
import com.fiapx.core.domain.services.utils.CommandExecutor;
import com.fiapx.core.domain.services.utils.DirectoryManager;
import com.fiapx.core.domain.services.utils.ZipFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
public class ProcessVideoUseCase implements IProcessVideoUseCase {
    @Autowired
    private IProcessingRequestRepository processingRequestRepository;

    @Value("${spring.application.upload-location}")
    private String uploadDir;

    @Value("${spring.application.output-location}")
    private String outputDir;

    @Async
    public void execute(ProcessingRequest request) {
        List<Path> frames = null;

        try {
            DirectoryManager.recreateDir("tempDir");
        }catch (DirectoryManagerException ex){
            request.setStatus(EProcessingStatus.ERROR);
            request.setErrorMessage(ex.getMessage());
            processingRequestRepository.save(request);
            return;
        }

        String framePattern = "tempDir/frame_%04d.png";
        String inputFile = Paths.get(uploadDir, request.getInputFileName()).toString();
        List<String> args = Arrays.asList("ffmpeg",
                "-i", inputFile,
                "-vf", "fps=1",
                "-y", framePattern);

        String output = CommandExecutor.executeCommand(args);
        try (Stream<Path> paths = Files.walk(Paths.get("tempDir"))) {
            frames = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".png"))
                    .toList();

            if(frames.isEmpty()){
                request.setStatus(EProcessingStatus.ERROR);
                request.setErrorMessage("Unable to extract frames. " + output);
                processingRequestRepository.save(request);
                return;
            }
        } catch (IOException e) {
            request.setStatus(EProcessingStatus.ERROR);
            request.setErrorMessage("Unable to read extracted frames. " + e.getMessage());
            processingRequestRepository.save(request);
            return;
        }

        try{
            request.setOutputFileName(String.format("%s.zip",request.getInputFileName()));
            Path zipFilePath = Path.of("output/" + request.getOutputFileName());
            ZipFileUtils.createZip(frames,zipFilePath);
        }catch (IOException e) {
            request.setStatus(EProcessingStatus.ERROR);
            request.setErrorMessage("Unable to create ZIP file. " + e.getMessage());
            processingRequestRepository.save(request);
            return;
        }

        request.setStatus(EProcessingStatus.COMPLETED);
        request.setCompletedAt(Timestamp.valueOf(LocalDateTime.now()));
        processingRequestRepository.save(request);
    }
}
