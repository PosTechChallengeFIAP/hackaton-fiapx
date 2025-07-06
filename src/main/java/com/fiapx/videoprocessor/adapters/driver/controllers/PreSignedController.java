package com.fiapx.videoprocessor.adapters.driver.controllers;

import com.fiapx.videoprocessor.adapters.driven.infra.storage.EStorageType;
import com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.config.EPreSignedUrlType;
import com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.service.ConfirmUploadToPreSignedUrlUseCase.IConfirmUploadToPreSignedUrlUseCase;
import com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.service.CreatePreSignedProcessingRequestUseCase.ICreatePreSignedProcessingRequestUseCase;
import com.fiapx.videoprocessor.adapters.driven.infra.storage.aws.s3.service.GeneratePreSignedUrlUseCase.IGeneratePreSignedUrlUseCase;
import com.fiapx.videoprocessor.core.application.exceptions.ValidationException;
import com.fiapx.videoprocessor.core.application.message.EMessageType;
import com.fiapx.videoprocessor.core.application.message.MessageResponse;
import com.fiapx.videoprocessor.core.domain.entities.*;
import com.fiapx.videoprocessor.core.domain.services.usecases.FindProcessingRequestByIdUseCase.IFindProcessingRequestByIdUseCase;
import com.fiapx.videoprocessor.core.domain.services.utils.DateUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.net.URL;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@RestController
@Tag(name = "Processing Request", description = "Endpoint to manage video processing requests" )
public class PreSignedController {

    @Autowired
    private ICreatePreSignedProcessingRequestUseCase createPreSignedProcessingRequestUseCase;

    @Autowired
    private IConfirmUploadToPreSignedUrlUseCase confirmUploadToPreSignedUrlUseCase;

    @Autowired
    private IGeneratePreSignedUrlUseCase generatePreSignedUrlUseCase;

    @Autowired
    private IFindProcessingRequestByIdUseCase findProcessingRequestByIdUseCase;

    @Value("${spring.application.upload-location}")
    private String uploadDir;

    @Value("${spring.application.output-location}")
    private String outputDir;

    @Value("${spring.application.storage}")
    private String storageType;

    @PostMapping("/process/presign/{extension}")
    @Operation(summary = "Create Processing Request", description = "This endpoint is used to create a new video " +
            "processing request using presigned S3 URL",
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
    public ResponseEntity newProcessingRequest(@PathVariable String extension){
        try {
            EStorageType storage = EStorageType.fromString(storageType);

            if(!EStorageType.AWS.equals(storage)){
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(MessageResponse.type(EMessageType.ERROR).withMessage("Pre Sign Service is only available for AWS Storage."));
            }

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            Timestamp now = Timestamp.valueOf(LocalDateTime.now());
            String uploadedFileName = String.format("%s_%s.%s", DateUtils.timestampToString(now), currentUser.getUsername(), extension);

            ProcessingRequest request = new ProcessingRequest();
            request.setCreatedAt(now);
            request.setStatus(EProcessingStatus.PRE_SIGNED);
            request.setInputFileName(uploadedFileName);
            request.setUsername(currentUser.getUsername());

            PreSignedResponse response = createPreSignedProcessingRequestUseCase.execute(request, uploadDir);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);
        } catch (ValidationException | DataIntegrityViolationException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(MessageResponse.type(EMessageType.ERROR).withMessage(ex.getMessage()));
        }
    }


    @PostMapping("/process/{id}/confirmUpload")
    @Operation(summary = "Create Processing Request", description = "This endpoint is used to inform that file was " +
            "uploaded to pre signed URL",
            tags = {"Process"},
            responses ={
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(schema = @Schema(implementation = ProcessingRequest.class))
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized Access", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity confirmUpload(@PathVariable String id, @RequestHeader("Authorization") String authHeader){
        try {
            String token = authHeader.startsWith("Bearer ") ? authHeader.substring(7) : authHeader;

            EStorageType storage = EStorageType.fromString(storageType);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            if(!EStorageType.AWS.equals(storage)){
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(MessageResponse.type(EMessageType.ERROR).withMessage("Pre Sign Service is only available for AWS Storage."));
            }

            ProcessingRequest request = confirmUploadToPreSignedUrlUseCase.execute(id, token);

            return ResponseEntity.ok(request);
        } catch (ValidationException | DataIntegrityViolationException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(MessageResponse.type(EMessageType.ERROR).withMessage(ex.getMessage()));
        }
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/process/{id}/download/presigned")
    @Operation(summary = "Download resulted ZIP file", description = "This endpoint is used to get the pre signed download " +
            "link from S3.",
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
    public ResponseEntity getDownloadLink(@PathVariable String id){
        try {
            EStorageType storage = EStorageType.fromString(storageType);

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            User currentUser = (User) auth.getPrincipal();

            if (!EStorageType.AWS.equals(storage)) {
                return ResponseEntity
                        .status(HttpStatus.FORBIDDEN)
                        .body(MessageResponse.type(EMessageType.ERROR).withMessage("Pre Sign Service is only available for AWS Storage."));
            }

            ProcessingRequest request = findProcessingRequestByIdUseCase.execute(id);

            URL url = generatePreSignedUrlUseCase.generatePreSignedUploadUrl(outputDir, request.getOutputFileName(), EPreSignedUrlType.DOWNLOAD);

            return ResponseEntity.ok(MessageResponse.type(EMessageType.SUCCESS).withMessage(url.toString()));
        }catch (RuntimeException ex){
            return ResponseEntity.badRequest().body(MessageResponse.type(EMessageType.ERROR).withMessage(ex.getMessage()));
        }

    }
}
