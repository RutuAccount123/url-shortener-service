package com.infracloud.urlshortener.service;

import java.util.List;
import java.util.Map;

public interface DomainMetricsService {
	List<Map.Entry<String, Integer>> getTopThreeDomains();
}
