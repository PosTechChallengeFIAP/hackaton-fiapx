package com.fiapx.videoprocessor.adapters.driver.controllers;

import com.fiapx.videoprocessor.core.application.exceptions.ResourceNotFoundException;
import com.fiapx.videoprocessor.core.application.exceptions.ValidationException;
import com.fiapx.videoprocessor.core.application.message.EMessageType;
import com.fiapx.videoprocessor.core.application.message.MessageResponse;
import com.fiapx.videoprocessor.core.domain.entities.EProcessingStatus;
import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;
import com.fiapx.videoprocessor.core.domain.services.usecases.CreateProcessingRequestUseCase.ICreateProcessingRequestUseCase;
import com.fiapx.videoprocessor.core.domain.services.usecases.FindProcessingRequestByIdUseCase.IFindProcessingRequestByIdUseCase;
import com.fiapx.videoprocessor.core.domain.services.usecases.FindProcessingRequestsByStatusUseCase.IFindProcessingRequestsByStatusUseCase;
import com.fiapx.videoprocessor.core.domain.services.usecases.FindProcessingRequestsUseCase.IFindProcessingRequestsUseCase;
import com.fiapx.videoprocessor.core.domain.services.usecases.GetFileUseCase.IGetFileUseCase;
import com.fiapx.videoprocessor.core.domain.services.usecases.ProcessVideoUseCase.IProcessVideoUseCase;
import com.fiapx.videoprocessor.core.domain.services.usecases.SaveFileUseCase.ISaveFileUseCase;
import com.fiapx.videoprocessor.core.domain.services.utils.DateUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@Tag(name = "Processing Request", description = "Endpoint to manage video processing requests" )
public class ProcessingRequestController {

    @Autowired
    private ICreateProcessingRequestUseCase createProcessingRequestUseCase;

    @Autowired
    private ISaveFileUseCase saveUploadedFileUseCase;

    @Autowired
    private IGetFileUseCase getFileUseCase;

    @Autowired
    private IProcessVideoUseCase processVideoUseCase;

    @Autowired
    private IFindProcessingRequestsUseCase findProcessingRequestsUseCase;

    @Autowired
    private IFindProcessingRequestByIdUseCase findProcessingRequestByIdUseCase;

    @Autowired
    private IFindProcessingRequestsByStatusUseCase findProcessingRequestsByStatusUseCase;

    @Value("${spring.application.upload-location}")
    private String uploadDir;

    @Value("${spring.application.output-location}")
    private String outputDir;

    @SuppressWarnings("rawtypes")
    @PostMapping("/process")
    @Operation(summary = "Create Processing Request", description = "This endpoint is used to create a new video " +
            "processing request",
            tags = {"Process"},
            responses ={
                    @ApiResponse(description = "Created", responseCode = "201",
                            content = {
                                    @Content(schema = @Schema(implementation = ProcessingRequest.class))
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized Access", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity newProcessingRequest(@RequestParam("file") MultipartFile file){
        try {
            if (file.isEmpty()) {
                return ResponseEntity
                        .status(HttpStatus.BAD_REQUEST)
                        .body(MessageResponse.type(EMessageType.ERROR)
                        .withMessage("Video to be processed is missing from the request"));
            }

            String fileName = file.getOriginalFilename();
            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            String uploadedFileName = String.format("%s_%s", DateUtils.timestampToString(now), fileName);
            saveUploadedFileUseCase.execute(uploadedFileName, file.getInputStream(), file.getSize(),uploadDir, false);

            ProcessingRequest request = new ProcessingRequest();
            request.setCreatedAt(now);
            request.setStatus(EProcessingStatus.IN_PROGRESS);
            request.setInputFileName(uploadedFileName);

            request = createProcessingRequestUseCase.execute(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(MessageResponse.type(EMessageType.SUCCESS).withMessage(request.getId().toString()));
        } catch (ValidationException | DataIntegrityViolationException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(MessageResponse.type(EMessageType.ERROR).withMessage(ex.getMessage()));
        }catch (IOException ex){
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(MessageResponse.type(EMessageType.ERROR).withMessage("Unable to read uploaded file."));
        }
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/process")
    @Operation(summary = "Finds Processing Requests", description = "This endpoint is used to find processing requests." +
            " It can be filtered by status.",
            tags = {"Process"},
            responses ={
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(mediaType = "application/json",array = @ArraySchema(schema = @Schema(implementation = ProcessingRequest.class)))
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized Access", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity all(@RequestParam(required = false) EProcessingStatus status){
        if(Objects.isNull(status)){
            return ResponseEntity.status(HttpStatus.OK).body(findProcessingRequestsUseCase.execute());
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(findProcessingRequestsByStatusUseCase.execute(status));
        }
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/process/{id}")
    @Operation(summary = "Finds processing request by Id", description = "This endpoint is used to find processing request" +
            " by Id",
            tags = {"Process"},
            responses ={
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = ProcessingRequest.class))
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized Access", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity one(@PathVariable String id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(findProcessingRequestByIdUseCase.execute(id));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(MessageResponse.type(EMessageType.ERROR).withMessage(ex.getMessage()));
        } catch (ValidationException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(MessageResponse.type(EMessageType.ERROR).withMessage(ex.getMessage()));
        }
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/process/{id}")
    @Operation(summary = "Finds processing request by Id", description = "This endpoint is used to find processing request" +
            " by Id",
            tags = {"Process"},
            responses ={
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = ProcessingRequest.class))
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized Access", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity doProcessing(@PathVariable String id) {
        ProcessingRequest request = findProcessingRequestByIdUseCase.execute(id);

        request = processVideoUseCase.execute(request);

        if(EProcessingStatus.ERROR.equals(request.getStatus())){
            return ResponseEntity.unprocessableEntity()
                    .body(MessageResponse.type(EMessageType.ERROR).withMessage(request.getErrorMessage()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(request);
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/process/{id}/download")
    @Operation(summary = "Download resulted ZIP file", description = "This endpoint is used to fDownload resulted ZIP file.",
            tags = {"Process"},
            responses ={
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(mediaType = "application/json",array = @ArraySchema(schema = @Schema(implementation = ProcessingRequest.class)))
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized Access", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity downloadZipFile(@PathVariable String id){
        ProcessingRequest request = findProcessingRequestByIdUseCase.execute(id);

        File file = getFileUseCase.execute(outputDir, request.getOutputFileName());
        Path filePath = file.toPath();
        Resource resource = null;

        try {
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(MessageResponse.type(EMessageType.ERROR).withMessage(e.getMessage()));
        }

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + filePath.getFileName().toString())
                .body(resource);

    }
}

