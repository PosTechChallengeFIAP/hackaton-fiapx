package com.fiapx.adapters.driver.controllers;

import com.fiapx.core.application.exceptions.ResourceNotFoundException;
import com.fiapx.core.application.exceptions.ValidationException;
import com.fiapx.core.application.message.EMessageType;
import com.fiapx.core.application.message.MessageResponse;
import com.fiapx.core.domain.entities.EProcessingStatus;
import com.fiapx.core.domain.entities.ProcessingRequest;
import com.fiapx.core.domain.services.CreateProcessingRequestUseCase.ICreateProcessingRequestUseCase;
import com.fiapx.core.domain.services.FindProcessingRequestByIdUseCase.IFindProcessingRequestByIdUseCase;
import com.fiapx.core.domain.services.FindProcessingRequestsByStatusUseCase.IFindProcessingRequestsByStatusUseCase;
import com.fiapx.core.domain.services.FindProcessingRequestsUseCase.IFindProcessingRequestsUseCase;
import com.fiapx.core.domain.services.ProcessVideoUseCase.IProcessVideoUseCase;
import com.fiapx.core.domain.services.SaveUploadedFileUseCase.ISaveUploadedFileUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Objects;

@RestController
@Tag(name = "Processing Request", description = "Endpoint to manage video processing requests" )
public class ProcessingRequestController {

    @Autowired
    private ICreateProcessingRequestUseCase createProcessingRequestUseCase;

    @Autowired
    private ISaveUploadedFileUseCase saveUploadedFileUseCase;

    @Autowired
    private IProcessVideoUseCase processVideoUseCase;

    @Autowired
    private IFindProcessingRequestsUseCase findProcessingRequestsUseCase;

    @Autowired
    private IFindProcessingRequestByIdUseCase findProcessingRequestByIdUseCase;

    @Autowired
    private IFindProcessingRequestsByStatusUseCase findProcessingRequestsByStatusUseCase;

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
            saveUploadedFileUseCase.execute(fileName, file.getInputStream());

            ProcessingRequest request = new ProcessingRequest();
            request.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));
            request.setStatus(EProcessingStatus.IN_PROGRESS);
            request.setInputFileName(String.format("%s_%s",request.getCreated_at().toString(), fileName));

            request = createProcessingRequestUseCase.execute(request);

            processVideoUseCase.execute(request);

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

        Path filePath = Paths.get("output/" + request.getOutputFileName());
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

