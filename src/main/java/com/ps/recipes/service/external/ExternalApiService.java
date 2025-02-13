package com.ps.recipes.service.external;

import com.ps.recipes.domain.Recipe;
import com.ps.recipes.dto.LoaderResponseDTO;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class ExternalApiService {

    private static final Logger log = LoggerFactory.getLogger(ExternalApiService.class);

    @Value("${external.recipes.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;

    private final io.github.resilience4j.circuitbreaker.CircuitBreaker circuitBreaker;

    public ExternalApiService(RestTemplate restTemplate, CircuitBreakerRegistry circuitBreakerRegistry) {
        this.restTemplate = restTemplate;
        this.circuitBreaker = circuitBreakerRegistry.circuitBreaker("recipeService");
        this.circuitBreaker.reset();
    }

    @CircuitBreaker(name = "recipeService", fallbackMethod = "fallbackFetchRecipes")
    @Retry(name = "recipeService")
    public List<Recipe> fetchRecipes() {
        log.info("Fetching recipes from external API");
        LoaderResponseDTO response = restTemplate.getForObject(apiUrl, LoaderResponseDTO.class);
        return response != null ? response.getRecipes() : Collections.emptyList();
    }

    // Fallback method when circuit breaker is open
    public List<Recipe> fallbackFetchRecipes(Exception ex) {
        log.info("Circuit breaker is open. Fallback method called");
        return Collections.emptyList();

    }
}




















