package com.dayo.careround.patientservice.exception;

public class WardServiceUnavailableException extends RuntimeException {
    public WardServiceUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
