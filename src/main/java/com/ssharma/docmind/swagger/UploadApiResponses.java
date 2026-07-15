package com.ssharma.docmind.swagger;

import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(
        responseCode = "200",
        description = "Document uploaded successfully"
)

@ApiResponse(
        responseCode = "400",
        description = "Validation failed"
)

@ApiResponse(
        responseCode = "415",
        description = "Unsupported document type"
)

@ApiResponse(
        responseCode = "500",
        description = "Internal server error"
)
public @interface UploadApiResponses {
}