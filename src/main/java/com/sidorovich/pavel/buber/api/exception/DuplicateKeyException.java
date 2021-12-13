package com.sidorovich.pavel.buber.api.exception;

public class DuplicateKeyException extends Exception {

    private final String attribute;

    public DuplicateKeyException(String attribute, String message) {
        super(message);
        this.attribute = attribute;
    }

    public String getAttribute() {
        return attribute;
    }

}
