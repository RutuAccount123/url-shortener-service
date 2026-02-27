package com.infracloud.urlshortener.repository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Repository;

@Repository
public class DomainMetricsRepository {

    private final Map<String, Integer> domainCountMap = new ConcurrentHashMap<>();

    // Increment domain count
    public void incrementDomain(String domain) {
        domainCountMap.merge(domain, 1, Integer::sum);
    }

    // Get a snapshot of domain counts
    public Map<String, Integer> getAllDomainCounts() {
        return Map.copyOf(domainCountMap);
    }
}