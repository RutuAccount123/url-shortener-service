package com.infracloud.urlshortener.repository;
import com.infracloud.urlshortener.model.UrlMapping;
import org.springframework.stereotype.Repository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UrlRepository {

    private final Map<String, UrlMapping> originalUrlMap =
            new ConcurrentHashMap<>();
    private final Map<String, UrlMapping> shortCodeMap =
            new ConcurrentHashMap<>();

    public Optional<UrlMapping> findByOriginalUrl(String originalUrl) {
        return Optional.ofNullable(originalUrlMap.get(originalUrl));
    }
    
    public Optional<UrlMapping> findByShortCode(String shortCode) {
        return Optional.ofNullable(shortCodeMap.get(shortCode));
    }

    public void save(UrlMapping mapping) {
        originalUrlMap.put(mapping.getOriginalUrl(), mapping);
        shortCodeMap.put(mapping.getShortCode(), mapping);
       
    }
    

}