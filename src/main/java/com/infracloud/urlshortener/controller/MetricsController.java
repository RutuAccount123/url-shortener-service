package com.infracloud.urlshortener.controller;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infracloud.urlshortener.service.DomainMetricsService;

@RestController
@RequestMapping("/api/metrics")
public class MetricsController {

    private static final Logger log = LoggerFactory.getLogger(MetricsController.class);

    @Autowired
    private DomainMetricsService metricsService;

    // GET: Return top 3 most shortened domains
    @GetMapping("/top-domains")
    public Map<String, Integer> getTopDomains() {
        log.info("Request received: GET /top-domains");

        List<Map.Entry<String, Integer>> topDomainsList = metricsService.getTopThreeDomains();

        Map<String, Integer> topDomains = topDomainsList.stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldVal, newVal) -> oldVal,
                        LinkedHashMap::new
                ));

        log.info("Returning top domains: {}", topDomains);
        return topDomains;
    }
}