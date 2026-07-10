package com.asset.util;

public class AssetNotAvailableException extends Exception {

    public AssetNotAvailableException(String message) {
        super(message);
    }

    @Override
    public String toString() {
        return getMessage();
    }
}