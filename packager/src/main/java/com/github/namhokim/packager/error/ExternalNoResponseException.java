package com.github.namhokim.packager.error;

public class ExternalNoResponseException extends RuntimeException {
    public ExternalNoResponseException(String message) {
        super(message);
    }
}
