package com.binarfud.binarfud_challenge6.exception;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super(message+" not found");
    }
}
