package com.ps.recipes.dto;

import lombok.Builder;

import java.util.List;


public record RecipeDTO(
        Long id,
        String name,
        Integer prepTimeMinutes,
        Integer cookTimeMinutes,
        Integer servings,
        String difficulty,
        String cuisine,
        Integer caloriesPerServing,
        Long userId,
        String image,
        Double rating,
        Integer reviewCount,
        List<String> ingredients,
        List<String> instructions,
        List<String> tags,
        List<String> mealType
) {
    @Builder
    public RecipeDTO {}
}
