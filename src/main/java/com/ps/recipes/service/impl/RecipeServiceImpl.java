package com.ps.recipes.service.impl;


import com.ps.recipes.domain.Recipe;
import com.ps.recipes.dto.RecipeDTO;
import com.ps.recipes.exception.RecipeNotFoundException;
import com.ps.recipes.repository.RecipeRepository;
import com.ps.recipes.service.RecipeService;
import com.ps.recipes.service.external.ExternalApiService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeServiceImpl implements RecipeService {


    private final ModelMapper modelMapper;

    private final RecipeRepository recipeRepository;

    private final ExternalApiService externalApiService;

    @PersistenceContext
    private EntityManager entityManager;


    @Transactional
    public Optional<RecipeDTO> findOne(Long id) {
        log.debug("Request to get Recipe : {}", id);
        Recipe recipe = recipeRepository.findById(id).orElseThrow(() -> new RecipeNotFoundException("Recipe not found"));
        return Optional.of(modelMapper.map(recipe, RecipeDTO.class));
    }


    @Transactional
    public List<RecipeDTO> findAll() {
        log.debug("Request to get all Recipes");
        return recipeRepository.findAll().stream().map(recipe -> modelMapper.map(recipe, RecipeDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public List<RecipeDTO> searchByText(String text) {
        log.debug("Request to search for a page of Recipes for query {}", text);
        SearchSession
                searchSession = Search.session(entityManager);
        SearchResult<Recipe> result = searchSession.search(Recipe.class)
                .where(f -> f.match()
                        .fields("name", "cuisine", "tags", "ingredients","difficulty","instructions","tags","mealType") // Fields to search
                        .matching(text)
                        .fuzzy(2)) // Optional: Enable fuzzy search
                .fetch(20);
        return result.hits().stream().map(recipe -> modelMapper.map(recipe, RecipeDTO.class)).toList();
    }

    @Transactional
    public List<RecipeDTO> loadAll() {
        log.debug("Request to load all recipes");
        List<Recipe> recipes = externalApiService.fetchRecipes();
        return recipeRepository.saveAll(recipes).stream().map(recipe -> modelMapper.map(recipe, RecipeDTO.class)).collect(Collectors.toList());
    }
}
















