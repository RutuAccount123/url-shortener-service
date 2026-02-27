package com.infracloud.urlshortener.serviceimpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.infracloud.urlshortener.exception.InvalidUrlException;
import com.infracloud.urlshortener.model.UrlMapping;
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

    public UrlShortenerServiceImpl(UrlRepository repository) {
        this.repository = repository;
    }

    @Override
    public String shortenUrl(String originalUrl) {

        log.info("Received request to shorten URL: {}", originalUrl);

        // Validate URL
        validateUrl(originalUrl);

        // Check if URL already shortened
        Optional<UrlMapping> existing = repository.findByOriginalUrl(originalUrl);
        if (existing.isPresent()) {
            return baseUrl + existing.get().getShortCode();
        }

        // Generate new short code
        String shortCode = ShortCodeGenerator.generate();
        UrlMapping mapping = new UrlMapping(originalUrl, shortCode);

        // Save in repository
        repository.save(mapping);

        log.info("Generated short code {} for URL {}", shortCode, originalUrl);

        return baseUrl + shortCode;
    }

    private void validateUrl(String url) {
        try {
            URI uri = new URI(url);
            if (uri.getScheme() == null ||
                (!"http".equalsIgnoreCase(uri.getScheme()) &&
                 !"https".equalsIgnoreCase(uri.getScheme())) ||
                uri.getHost() == null) {
                throw new InvalidUrlException("Invalid URL format");
            }
        } catch (Exception ex) {
            log.error("URL validation failed for {}", url);
            throw new InvalidUrlException("Invalid URL format");
        }
    }
}