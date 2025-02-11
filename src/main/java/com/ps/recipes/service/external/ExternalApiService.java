package com.ps.recipes.service.external;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ps.recipes.domain.Recipe;
import com.ps.recipes.dto.LoaderResponseDTO;
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
@RequiredArgsConstructor
public class ExternalApiService {

    private static final Logger log = LoggerFactory.getLogger(ExternalApiService.class);

    @Value("${external.recipes.url}")
    private String apiUrl;

    private final RestTemplate restTemplate;


    public List<Recipe> fetchRecipes() {
        LoaderResponseDTO response = restTemplate.getForObject(apiUrl, LoaderResponseDTO.class);
        return response.getRecipes();
    }
}




















