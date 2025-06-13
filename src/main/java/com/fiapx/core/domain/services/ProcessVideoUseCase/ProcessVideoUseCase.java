package com.fiapx.core.domain.services.ProcessVideoUseCase;

import com.fiapx.core.application.exceptions.DirectoryManagerException;
import com.fiapx.core.application.exceptions.VideoProcessingException;
import com.fiapx.core.domain.entities.EProcessingStatus;
import com.fiapx.core.domain.entities.ProcessingRequest;
import com.fiapx.core.domain.repositories.IProcessingRequestRepository;
import com.fiapx.core.domain.services.utils.CommandExecutor;
import com.fiapx.core.domain.services.utils.DirectoryManager;
import com.fiapx.core.domain.services.utils.ZipFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ProcessVideoUseCase implements IProcessVideoUseCase {
    @Autowired
    private IProcessingRequestRepository processingRequestRepository;

    @Async
    public ProcessingRequest execute(ProcessingRequest request) {
        List<Path> frames;

        try {
            DirectoryManager.recreateDir("tempDir");
        }catch (DirectoryManagerException ex){
            throw new VideoProcessingException(ex.getMessage());
        }

        String framePattern = "tempDir/frame_%04d.png";
        String inputFile = "uploads/" + request.getInputFileName();
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
                throw new VideoProcessingException("Unable to extract frames");
            }
        } catch (IOException e) {
            throw new VideoProcessingException("Unable to read extracted frames. " + e.getMessage());
        }

        try{
            request.setOutputFileName(String.format("%s.zip",request.getInputFileName()));
            Path zipFilePath = Path.of("output/" + request.getOutputFileName());
            ZipFileUtils.createZip(frames,zipFilePath);
        }catch (IOException e) {
            throw new VideoProcessingException("Unable to create ZIP file. " + e.getMessage());
        }

        request.setStatus(EProcessingStatus.COMPLETED);
        return request;
    }
}
