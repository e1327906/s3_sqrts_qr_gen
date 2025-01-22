package com.qre.cmel.exception;

public class RequesterNotFoundException extends RuntimeException {
    public RequesterNotFoundException(String message) {
        super(message);
    }
}
