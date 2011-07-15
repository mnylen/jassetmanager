package org.jassetmanager;

public class AssetException extends RuntimeException {
    public AssetException(String message) {
        super(message);
    }

    public AssetException(String message, Throwable cause) {
        super(message, cause);
    }

    public AssetException(Throwable cause) {
        super(cause);
    }
}
