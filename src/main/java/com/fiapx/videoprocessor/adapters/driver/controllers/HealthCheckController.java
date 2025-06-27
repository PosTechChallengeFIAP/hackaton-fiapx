package com.fiapx.videoprocessor.adapters.driver.controllers;

import com.fiapx.videoprocessor.core.domain.entities.ProcessingRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "Health Check", description = "Endpoint to check system health" )
public class HealthCheckController {

    @SuppressWarnings("rawtypes")
    @GetMapping("/healthz")
    @Operation(summary = "Check Health", description = "This endpoint is used to check system health.",
            tags = {"Health"},
            responses ={
                    @ApiResponse(description = "Success", responseCode = "200",
                            content = {
                                    @Content(mediaType = "application/json",array = @ArraySchema(schema = @Schema(implementation = String.class)))
                            })
            }
    )
    public ResponseEntity health(){
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

