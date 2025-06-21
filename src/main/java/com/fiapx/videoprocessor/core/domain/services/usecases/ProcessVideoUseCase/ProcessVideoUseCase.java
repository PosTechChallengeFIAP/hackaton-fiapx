package com.fiapx.videoprocessor.core.domain.services.usecases.ProcessVideoUseCase;

import com.fiapx.videoprocessor.core.application.exceptions.DirectoryManagerException;
import com.fiapx.videoprocessor.core.domain.entities.EProcessingStatus;
import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;
import com.fiapx.videoprocessor.core.domain.repositories.IProcessingRequestRepository;
import com.fiapx.videoprocessor.core.domain.services.usecases.GetFileUseCase.IGetFileUseCase;
import com.fiapx.videoprocessor.core.domain.services.usecases.SaveFileUseCase.ISaveFileUseCase;
import com.fiapx.videoprocessor.core.domain.services.utils.CommandExecutor;
import com.fiapx.videoprocessor.core.domain.services.utils.DirectoryManager;
import com.fiapx.videoprocessor.core.domain.services.utils.ZipFileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
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

    @Autowired
    private ISaveFileUseCase saveFileUseCase;

    @Autowired
    private IGetFileUseCase getFileUseCase;

    @Value("${spring.application.upload-location}")
    private String uploadDir;

    @Value("${spring.application.output-location}")
    private String outputDir;

    public ProcessingRequest execute(ProcessingRequest request) {
        List<Path> frames = null;
        String tempFolderPath = "tempDir/processing/" + request.getId().toString();

        try {
            DirectoryManager.createDir(tempFolderPath);
        }catch (DirectoryManagerException ex){
            request.setStatus(EProcessingStatus.ERROR);
            request.setErrorMessage(ex.getMessage());
            return processingRequestRepository.save(request);
        }

        String framePattern = tempFolderPath + "/frame_%04d.png";
        File file = getFileUseCase.execute(uploadDir, request.getInputFileName());
        String inputFile = file.getPath();
        List<String> args = Arrays.asList("ffmpeg",
                "-i", inputFile,
                "-vf", "fps=1",
                "-y", framePattern);

        String output = CommandExecutor.executeCommand(args);
        try (Stream<Path> paths = Files.walk(Paths.get(tempFolderPath))) {
            frames = paths
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".png"))
                    .toList();

            if(frames.isEmpty()){
                request.setStatus(EProcessingStatus.ERROR);
                request.setErrorMessage("Unable to extract frames. " + output);
                return processingRequestRepository.save(request);
            }
        } catch (IOException e) {
            request.setStatus(EProcessingStatus.ERROR);
            request.setErrorMessage("Unable to read extracted frames. " + e.getMessage());
            return processingRequestRepository.save(request);
        }

        try{
            request.setOutputFileName(String.format("%s.zip",request.getInputFileName()));
            Path zipFilePath = Paths.get(outputDir, request.getOutputFileName());
            File zipFile = ZipFileUtils.createZip(frames,zipFilePath);

            saveFileUseCase.execute(request.getOutputFileName(), new FileInputStream(zipFile), outputDir, true);
        }catch (IOException e) {
            request.setStatus(EProcessingStatus.ERROR);
            request.setErrorMessage("Unable to create ZIP file. " + e.getMessage());
            return processingRequestRepository.save(request);
        }

        request.setStatus(EProcessingStatus.COMPLETED);
        request.setCompletedAt(Timestamp.valueOf(LocalDateTime.now()));
        request.setErrorMessage(null);
        return processingRequestRepository.save(request);
    }
}
