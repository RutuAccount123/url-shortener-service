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

    public Optional<UrlMapping> findByOriginalUrl(String originalUrl) {
        return Optional.ofNullable(originalUrlMap.get(originalUrl));
    }

    public void save(UrlMapping mapping) {
        originalUrlMap.put(mapping.getOriginalUrl(), mapping);
       
    }
}