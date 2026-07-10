package com.asset.util;

public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}