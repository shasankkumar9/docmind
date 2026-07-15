package com.ssharma.docmind.exception;

public class ParsingException extends DocMindException {

    public ParsingException(String message, Throwable cause) {
        super(ErrorCode.DOCUMENT_PARSING_FAILED, message, cause);
    }

}