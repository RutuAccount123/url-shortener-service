package com.infracloud.urlshortener.serviceimpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.infracloud.urlshortener.exception.DomainExtractionException;
import com.infracloud.urlshortener.exception.InvalidUrlException;
import com.infracloud.urlshortener.exception.ShortUrlNotFoundException;
import com.infracloud.urlshortener.model.UrlMapping;
import com.infracloud.urlshortener.repository.DomainMetricsRepository;
import com.infracloud.urlshortener.repository.UrlRepository;
import com.infracloud.urlshortener.service.UrlShortenerService;
import com.infracloud.urlshortener.util.ShortCodeGenerator;
import java.net.URI;
import java.util.Optional;

@Service
public class UrlShortenerServiceImpl implements UrlShortenerService {

    @Value("${app.short-url.base-url}")
    private String baseUrl;

    private static final Logger log =
            LoggerFactory.getLogger(UrlShortenerServiceImpl.class);

    private final UrlRepository repository;
    private final DomainMetricsRepository metricsRepository;

    public UrlShortenerServiceImpl(UrlRepository repository, DomainMetricsRepository metricsRepository) {
        this.repository = repository;
        this.metricsRepository = metricsRepository;
    }

    @Override
    public String shortenUrl(String originalUrl) {

        log.info("Received request to shorten URL: {}", originalUrl);

        // Validate URL
        validateUrl(originalUrl);
        
        URI uri = URI.create(originalUrl);
        String domain = uri.getHost();
        if (domain == null || domain.isBlank()) {
            log.error("Domain extraction failed for URL: {}", originalUrl);
            throw new DomainExtractionException("Domain could not be extracted from URL: " + originalUrl);
        }

        metricsRepository.incrementDomain(domain);
        log.info("Incremented domain count for '{}'", domain);

        // Check if URL already shortened
        Optional<UrlMapping> existing = repository.findByOriginalUrl(originalUrl);
        if (existing.isPresent()) {
            log.info("URL already shortened. Returning existing short URL: {}{}", baseUrl, existing.get().getShortCode());
            return baseUrl + existing.get().getShortCode();
        }
        
        Optional<UrlMapping> existingurl = repository.findByOriginalUrl(originalUrl);
        if (existing.isPresent()) {
            // Increment domain count even for existing URL
            metricsRepository.incrementDomain(domain);
            log.info("URL already shortened. Incremented domain count for '{}'", domain);
            log.info("Returning existing short URL: {}{}", baseUrl, existingurl.get().getShortCode());
            return baseUrl + existingurl.get().getShortCode();
        }

        // Generate new short code
        String shortCode = ShortCodeGenerator.generate();
        UrlMapping mapping = new UrlMapping(originalUrl, shortCode);

        // Save in repository
        repository.save(mapping);

        
        log.info("Generated new short code '{}' for URL '{}'", shortCode, originalUrl);

        return baseUrl + shortCode;
    }

    private void validateUrl(String url) {
        try {
            URI uri = new URI(url);
            if (uri.getScheme() == null ||
                (!"http".equalsIgnoreCase(uri.getScheme()) &&
                 !"https".equalsIgnoreCase(uri.getScheme())) ||
                uri.getHost() == null) {

                log.warn("Validation failed for URL: {}", url);
                throw new InvalidUrlException("Invalid URL format");
            }
        } catch (Exception ex) {
            log.error("URL validation threw exception for '{}': {}", url, ex.getMessage());
            throw new InvalidUrlException("Invalid URL format");
        }
    }

    @Override
    public String getOriginalUrl(String shortCode) {

        log.info("Fetching original URL for short code: {}", shortCode);

        return repository.findByShortCode(shortCode)
                .map(original -> {
                    log.info("Found original URL '{}' for short code '{}'", original.getOriginalUrl(), shortCode);
                    return original.getOriginalUrl();
                })
                .orElseThrow(() -> {
                    log.error("Short URL not found for code: {}", shortCode);
                    return new ShortUrlNotFoundException("Short URL not found");
                });
    }
}