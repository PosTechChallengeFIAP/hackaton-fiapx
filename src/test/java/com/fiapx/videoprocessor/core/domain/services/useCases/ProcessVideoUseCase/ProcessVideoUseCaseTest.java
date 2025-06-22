package com.fiapx.videoprocessor.core.domain.services.useCases.ProcessVideoUseCase;

import com.fiapx.videoprocessor.core.application.exceptions.DirectoryManagerException;
import com.fiapx.videoprocessor.core.domain.entities.EProcessingStatus;
import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;
import com.fiapx.videoprocessor.core.domain.repositories.IProcessingRequestRepository;
import com.fiapx.videoprocessor.core.domain.services.usecases.GetFileUseCase.IGetFileUseCase;
import com.fiapx.videoprocessor.core.domain.services.usecases.ProcessVideoUseCase.ProcessVideoUseCase;
import com.fiapx.videoprocessor.core.domain.services.usecases.SaveFileUseCase.ISaveFileUseCase;
import com.fiapx.videoprocessor.core.domain.services.utils.CommandExecutor;
import com.fiapx.videoprocessor.core.domain.services.utils.DirectoryManager;
import com.fiapx.videoprocessor.core.domain.services.utils.ZipFileUtils;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ProcessVideoUseCaseTest {

    @InjectMocks
    private ProcessVideoUseCase useCase;

    @Mock
    private IProcessingRequestRepository repository;

    @Mock
    private ISaveFileUseCase saveFileUseCase;

    @Mock
    private IGetFileUseCase getFileUseCase;

    @BeforeEach
    void setUp() {
        useCase.uploadDir = "upload";
        useCase.outputDir = "output";
    }

    @Test
    void errorWhenCreatingDirectory() {
        ProcessingRequest req = new ProcessingRequest();
        req.setId(UUID.randomUUID());

        try (MockedStatic<DirectoryManager> mocked = mockStatic(DirectoryManager.class)) {
            mocked.when(() -> DirectoryManager.createDir(anyString()))
                    .thenThrow(new DirectoryManagerException("erro"));

            when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

            ProcessingRequest res = useCase.execute(req);

            assertEquals(EProcessingStatus.ERROR, res.getStatus());
            assertEquals("erro", res.getErrorMessage());
        }
    }

    @Test
    void errorWhenNoExtractedFrame() {
        ProcessingRequest req = new ProcessingRequest();
        req.setId(UUID.randomUUID());
        req.setInputFileName("video.mp4");

        File fakeInputFile = mock(File.class);
        when(fakeInputFile.getPath()).thenReturn("video.mp4");

        when(getFileUseCase.execute(any(), any())).thenReturn(fakeInputFile);

        try (MockedStatic<DirectoryManager> dirMock = mockStatic(DirectoryManager.class);
             MockedStatic<CommandExecutor> cmdMock = mockStatic(CommandExecutor.class);
             MockedStatic<Files> filesMock = mockStatic(Files.class)) {

            Path fakeDir = Paths.get("temp");
            filesMock.when(() -> Files.walk(any())).thenReturn(Stream.empty());

            when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

            ProcessingRequest res = useCase.execute(req);
            assertEquals(EProcessingStatus.ERROR, res.getStatus());
            assertTrue(res.getErrorMessage().contains("Unable to extract frames"));
        } catch (Exception e) {
            fail("Erro mockando Files.walk");
        }
    }

    @Test
    void errorWhenFrameNonRead() {
        ProcessingRequest req = new ProcessingRequest();
        req.setId(UUID.randomUUID());
        req.setInputFileName("video.mp4");

        File fakeInputFile = mock(File.class);
        when(fakeInputFile.getPath()).thenReturn("video.mp4");

        when(getFileUseCase.execute(any(), any())).thenReturn(fakeInputFile);

        try (MockedStatic<DirectoryManager> dirMock = mockStatic(DirectoryManager.class);
             MockedStatic<CommandExecutor> cmdMock = mockStatic(CommandExecutor.class);
             MockedStatic<Files> filesMock = mockStatic(Files.class)) {

            filesMock.when(() -> Files.walk(any())).thenThrow(new IOException("IO error"));

            when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

            ProcessingRequest res = useCase.execute(req);
            assertEquals(EProcessingStatus.ERROR, res.getStatus());
            assertTrue(res.getErrorMessage().contains("Unable to read extracted frames"));
        }
    }

    @Test
    void errorWhenCreatingZip() {
        ProcessingRequest req = new ProcessingRequest();
        req.setId(UUID.randomUUID());
        req.setInputFileName("video.mp4");

        File fakeInputFile = mock(File.class);
        when(fakeInputFile.getPath()).thenReturn("video.mp4");

        when(getFileUseCase.execute(any(), any())).thenReturn(fakeInputFile);

        Path fakeFrame = Paths.get("frame1.png");

        try (MockedStatic<DirectoryManager> dirMock = mockStatic(DirectoryManager.class);
             MockedStatic<CommandExecutor> cmdMock = mockStatic(CommandExecutor.class);
             MockedStatic<Files> filesMock = mockStatic(Files.class);
             MockedStatic<ZipFileUtils> zipMock = mockStatic(ZipFileUtils.class)) {

            filesMock.when(() -> Files.walk(any())).thenReturn(Stream.of(fakeFrame));
            zipMock.when(() -> ZipFileUtils.createZip(anyList(), any())).thenThrow(new IOException("ZIP error"));

            when(repository.save(any())).thenAnswer(i -> i.getArgument(0));

            ProcessingRequest res = useCase.execute(req);
            assertEquals(EProcessingStatus.ERROR, res.getStatus());
            assertFalse(res.getErrorMessage().contains("Unable to create ZIP file"));
        } catch (Exception e) {
            fail("Erro mockando");
        }
    }


}
