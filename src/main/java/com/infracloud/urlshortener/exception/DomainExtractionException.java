package com.infracloud.urlshortener.exception;

public class DomainExtractionException extends RuntimeException {

    public DomainExtractionException(String message) {
        super(message);
    }
}