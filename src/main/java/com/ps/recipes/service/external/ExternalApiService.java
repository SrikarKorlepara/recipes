package com.ps.recipes.service.external;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ps.recipes.domain.Recipe;
import com.ps.recipes.dto.LoaderResponseDTO;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

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




















