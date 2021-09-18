package com.github.namhokim.packager.error;

public class ExternalConnectionException extends RuntimeException {
    public ExternalConnectionException(String message, Throwable cause) {
        super(message, cause);
    }
}
