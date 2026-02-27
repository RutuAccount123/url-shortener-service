package com.infracloud.urlshortener.service;

public interface UrlShortenerService {

    String shortenUrl(String originalUrl);
    String getOriginalUrl(String shortCode);
    
}