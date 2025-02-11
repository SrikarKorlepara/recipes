package com.ps.recipes.service;


import com.ps.recipes.dto.RecipeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class RecipeService {
    public RecipeDTO save(RecipeDTO recipeDTO) {
        return null;
    }

    public RecipeDTO update(RecipeDTO recipeDTO) {
        return null;
    }

    public Optional<RecipeDTO> partialUpdate(RecipeDTO recipeDTO) {
        return null;
    }

    public Optional<RecipeDTO> findOne(Long id) {
        return Optional.empty();
    }

    public void delete(Long id) {

    }
}
