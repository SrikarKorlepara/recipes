package com.ps.recipes.service;

import com.ps.recipes.dto.RecipeDTO;

import java.util.List;
import java.util.Optional;

public interface RecipeService {
    List<RecipeDTO> findAll();
    List<RecipeDTO> searchByText(String text);
    Optional<RecipeDTO> findOne(Long id);
    List<RecipeDTO> loadAll();
}
