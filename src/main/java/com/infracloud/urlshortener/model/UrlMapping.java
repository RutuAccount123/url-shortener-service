package com.infracloud.urlshortener.model;

public class UrlMapping {

    private final String originalUrl;
    private final String shortCodeOfUrl;

    public UrlMapping(String originalUrl, String shortCode) {
        this.originalUrl = originalUrl;
        this.shortCodeOfUrl = shortCode;
    }

    public String getOriginalUrl() {
        return originalUrl;
    }

    public String getShortCode() {
        return shortCodeOfUrl;
    }
}