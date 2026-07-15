package com.ssharma.docmind.exception;

public abstract class DocMindException extends RuntimeException {

    private final ErrorCode errorCode;

    protected DocMindException(ErrorCode errorCode,
                               String message) {

        super(message);

        this.errorCode = errorCode;
    }

    protected DocMindException(ErrorCode errorCode,
                               String message,
                               Throwable cause) {

        super(message, cause);

        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

}