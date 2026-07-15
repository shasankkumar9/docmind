package com.ssharma.docmind.exception;

public class UnsupportedFileTypeException extends DocMindException {

    public UnsupportedFileTypeException(String message) {
        super(ErrorCode.UNSUPPORTED_FILE_TYPE, message);
    }

}