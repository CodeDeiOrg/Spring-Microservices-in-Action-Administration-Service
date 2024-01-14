package com.onlinelibrary.administration.exception;

public class EmailMissingException extends RuntimeException {
    public EmailMissingException(String message) {
        super(message);
    }
}
