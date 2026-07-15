package com.ssharma.docmind.exception;

public class ChatException extends DocMindException {

    public ChatException(String message, Throwable cause) {
        super(ErrorCode.CHAT_FAILED, message, cause);
    }

}