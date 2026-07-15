package com.ssharma.docmind.exception;

public class RetrievalException extends DocMindException {

    public RetrievalException(String message, Throwable cause) {
        super(ErrorCode.RETRIEVAL_FAILED, message, cause);
    }

}