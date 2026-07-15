package com.ssharma.docmind.exception;

public enum ErrorCode {

    VALIDATION_ERROR("DOCMIND-400"),

    DOCUMENT_NOT_FOUND("DOCMIND-404"),

    UNSUPPORTED_FILE_TYPE("DOCMIND-415"),

    DOCUMENT_PARSING_FAILED("DOCMIND-422"),

    EMBEDDING_FAILED("DOCMIND-501"),

    RETRIEVAL_FAILED("DOCMIND-502"),

    CHAT_FAILED("DOCMIND-503"),

    INTERNAL_SERVER_ERROR("DOCMIND-500");

    private final String code;

    ErrorCode(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

}