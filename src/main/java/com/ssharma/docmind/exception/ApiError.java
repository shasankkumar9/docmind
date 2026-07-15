package com.ssharma.docmind.exception;

import java.time.LocalDateTime;

public record ApiError(

        String code,

        String message,

        LocalDateTime timestamp

) {

    public ApiError(ErrorCode errorCode,
                    String message,
                    LocalDateTime timestamp) {

        this(errorCode.code(), message, timestamp);

    }

}