package com.infracloud.urlshortener.serviceimpl;

import com.infracloud.urlshortener.repository.DomainMetricsRepository;
import com.infracloud.urlshortener.repository.UrlRepository;
import com.infracloud.urlshortener.service.DomainMetricsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class DomainMetricsServiceImpl implements DomainMetricsService {

    private final DomainMetricsRepository repository;

    public DomainMetricsServiceImpl(DomainMetricsRepository repository) {
        this.repository = repository;
    }

    public List<Map.Entry<String, Integer>> getTopThreeDomains() {
        // Read the map from repository (already thread-safe)
        return repository.getAllDomainCounts()
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue(Comparator.reverseOrder()))
                .limit(3) // always top 3
                .collect(Collectors.toList());
    }

	
}
