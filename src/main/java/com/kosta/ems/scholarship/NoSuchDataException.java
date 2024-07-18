package com.kosta.ems.scholarship;

public class NoSuchDataException extends RuntimeException {
    public NoSuchDataException(String message) {
        super(message);
    }
}
