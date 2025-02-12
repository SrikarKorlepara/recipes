package com.ps.recipes.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RecipeDTO implements Serializable {
    private Long id;
    private String name;
    private Integer prepTimeMinutes;
    private Integer cookTimeMinutes;
    private Integer servings;
    private String difficulty;
    private String cuisine;
    private Integer caloriesPerServing;
    private Long userId;
    private String image;
    private Double rating;
    private Integer reviewCount;
    private List<String> ingredients;
    private List<String> instructions;
    private List<String> tags;
    private List<String> mealType;
}
