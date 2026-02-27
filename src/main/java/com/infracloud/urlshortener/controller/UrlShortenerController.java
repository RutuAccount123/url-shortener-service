package com.infracloud.urlshortener.controller;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.infracloud.urlshortener.service.UrlShortenerService;

@RestController
@RequestMapping("/api")
public class UrlShortenerController {
	
 @Autowired
  public UrlShortenerService service;

   
    @PostMapping("/url/shorten")
    public String shortenUrl(@RequestBody Map<String, String> request) {
        return service.shortenUrl(request.get("url"));
    }
    
}