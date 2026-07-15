package com.ssharma.docmind.exception;

public class DocumentNotFoundException extends DocMindException {

    public DocumentNotFoundException(String message) {
        super(ErrorCode.DOCUMENT_NOT_FOUND, message);
    }

}