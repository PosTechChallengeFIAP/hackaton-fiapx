package com.fiapx.adapters.driver.controllers;

import com.fiapx.core.application.exceptions.ResourceNotFoundException;
import com.fiapx.core.application.exceptions.UsedProductCannotBeDeletedException;
import com.fiapx.core.application.exceptions.ValidationException;
import com.fiapx.core.application.message.EMessageType;
import com.fiapx.core.application.message.MessageResponse;
import com.fiapx.core.domain.entities.EProcessingStatus;
import com.fiapx.core.domain.entities.ProcessingRequest;
import com.fiapx.core.domain.services.CreateProcessingRequestUseCase.CreateProcessingRequestUseCase.ICreateProductUseCase;
import com.fiapx.core.domain.services.DeleteProductUseCase.IDeleteProductUseCase;
import com.fiapx.core.domain.services.FindProductByIdUseCase.IFindProductByIdUseCase;
import com.fiapx.core.domain.services.FindProductsByCategoryUseCase.IFindProductsByCategoryUseCase;
import com.fiapx.core.domain.services.FindProductsUseCase.IFindProductsUseCase;
import com.fiapx.core.domain.services.CreateProcessingRequestUseCase.ICreateProcessingRequestUseCase;
import com.fiapx.core.domain.services.UpdateProductUseCase.IUpdateProductUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@RestController
@Tag(name = "Processing Request", description = "Endpoint to manage video processing requests" )
public class ProcessingRequestController {

    @Autowired
    private ICreateProcessingRequestUseCase createProcessingRequestUseCase;

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

            ProcessingRequest request = new ProcessingRequest();
            request.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));
            request.setStatus(EProcessingStatus.IN_PROGRESS);
            request.setFilePath(String.format("uploads/%s_%s",request.getCreated_at().toString(), fileName));

            request = createProcessingRequestUseCase.execute(request);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(MessageResponse.type(EMessageType.SUCCESS).withMessage(request.getId().toString()));
        } catch (ValidationException | DataIntegrityViolationException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(MessageResponse.type(EMessageType.ERROR).withMessage(ex.getMessage()));
        }
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/productCategory")
    @Operation(summary = "Finds all product categories", description = "This endpoint is used to find all product categories",
            tags = {"Product"},
            responses ={
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(mediaType = "application/json",array = @ArraySchema(schema = @Schema(implementation = EProcessingStatus.class)))
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized Access", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Internal Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity all(){
        return ResponseEntity.status(HttpStatus.OK).body(Arrays.stream(EProcessingStatus.values()).toList());
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/product")
    @Operation(summary = "Finds products", description = "This endpoint is used to find products. " +
            "If the request has a category it returns the products from this category",
            tags = {"Product"},
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
    public ResponseEntity all(@RequestParam(required = false) EProcessingStatus category){
        if(Objects.isNull(category)){
            return ResponseEntity.status(HttpStatus.OK).body(findProductsUseCase.execute());
        }
        else{
            return ResponseEntity.status(HttpStatus.OK).body(findProductsByCategoryUseCase.execute(category));
        }
    }

    @SuppressWarnings("rawtypes")
    @GetMapping("/product/{id}")
    @Operation(summary = "Finds product by Id", description = "This endpoint is used to find product by Id",
            tags = {"Product"},
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
    public ResponseEntity one(@PathVariable UUID id) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(findProductByIdUseCase.execute(id));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(MessageResponse.type(EMessageType.ERROR).withMessage(ex.getMessage()));
        }
    }

    @SuppressWarnings("rawtypes")
    @PostMapping("/product")
    @Operation(summary = "Create product", description = "This endpoint is used to create a new product",
            tags = {"Product"},
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
    public ResponseEntity newProduct(@RequestBody ProcessingRequest product){
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(createProductUseCase.execute(product));
        } catch (ValidationException | DataIntegrityViolationException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(MessageResponse.type(EMessageType.ERROR).withMessage(ex.getMessage()));
        }
    }

    @SuppressWarnings("rawtypes")
    @PatchMapping("/product/{id}")
    @Operation(summary = "Update Product", description = "This endpoint is used to update product",
            tags = {"Product"},
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
    public ResponseEntity updateProduct(@PathVariable UUID id, @RequestBody ProcessingRequest product) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(updateProductUseCase.execute(id, product));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(MessageResponse.type(EMessageType.ERROR).withMessage(ex.getMessage()));
        } catch (ValidationException | IllegalAccessException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(MessageResponse.type(EMessageType.ERROR).withMessage(ex.getMessage()));
        }
    }

    @DeleteMapping("/product/{id}")
    @Operation(summary = "Delete Product", description = "This endpoint is used to Delete product",
            tags = {"Product"},
            responses ={
                    @ApiResponse(description = "No Content", responseCode = "204",
                            content = {
                                    @Content(schema = @Schema(implementation = MessageResponse.class))
                            }),
                    @ApiResponse(description = "Bad Request", responseCode = "400", content = @Content),
                    @ApiResponse(description = "Unauthorized Access", responseCode = "401", content = @Content),
                    @ApiResponse(description = "Not Found", responseCode = "404", content = @Content),
                    @ApiResponse(description = "Method Not Allowed", responseCode = "405", content = @Content),
                    @ApiResponse(description = "Internal  Error", responseCode = "500", content = @Content)
            }
    )
    public ResponseEntity<MessageResponse> deleteProduct(@PathVariable UUID id) {
        try {
            deleteProductUseCase.execute(id);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(MessageResponse.type(EMessageType.SUCCESS).withMessage("Product successfully deleted."));
        }catch (UsedProductCannotBeDeletedException ex){
            return ResponseEntity
                    .status(HttpStatus.METHOD_NOT_ALLOWED)
                    .body(MessageResponse.type(EMessageType.ERROR).withMessage(ex.getMessage()));
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(MessageResponse.type(EMessageType.ERROR).withMessage(ex.getMessage()));
        }
    }
}

