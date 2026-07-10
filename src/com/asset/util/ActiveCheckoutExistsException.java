package com.asset.util;

public class ActiveCheckoutExistsException extends Exception {

    public ActiveCheckoutExistsException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}