package com.ssharma.docmind.exception;

public class EmbeddingException extends DocMindException {

    public EmbeddingException(String message, Throwable cause) {
        super(ErrorCode.EMBEDDING_FAILED, message, cause);
    }

}