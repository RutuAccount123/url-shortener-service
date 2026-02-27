package com.infracloud.urlshortener.controller;
import java.net.URI;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.infracloud.urlshortener.service.UrlShortenerService;

@RestController
@RequestMapping("/urls")
public class UrlShortenerController {
	
    private static final Logger log = LoggerFactory.getLogger(UrlShortenerController.class);

	
 @Autowired
  public UrlShortenerService service;

   
    @PostMapping("/shorten")
    public ResponseEntity<String> shortenUrl(@RequestBody Map<String, String> request) {
    	String originalUrl = request.get("url");
        log.info("Received request to shorten URL: {}", originalUrl);

        String shortUrl = service.shortenUrl(originalUrl);
        log.info("Generated short URL: {}", shortUrl);

        return ResponseEntity.ok(shortUrl);
    }   
    
 // GET: Redirect short code to original URL
    @GetMapping("/original/{shortcode}")
    public ResponseEntity<Void> redirect(@PathVariable String shortcode) {
        log.info("Received request to redirect short code: {}", shortcode);

        String originalUrl = service.getOriginalUrl(shortcode);
        log.info("Redirecting to original URL: {}", originalUrl);

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(originalUrl));

        return new ResponseEntity<>(headers, HttpStatus.FOUND);
    }
}