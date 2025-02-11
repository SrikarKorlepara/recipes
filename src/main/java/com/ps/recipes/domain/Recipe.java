package com.ps.recipes.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "recipes")
@Getter
@Setter
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class Recipe implements Serializable {

    @Id
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "prep_time_minutes")
    private Integer prepTimeMinutes;

    @Column(name = "cook_time_minutes")
    private Integer cookTimeMinutes;

    @Column(nullable = false)
    private Integer servings;

    @Column(nullable = false)
    private String difficulty;

    @Column(nullable = false)
    private String cuisine;

    @Column(name = "calories_per_serving")
    private Integer caloriesPerServing;

    @Column(name = "user_id")
    private Long userId;

    @Column(nullable = false)
    private String image;

    @Column(nullable = false)
    private Double rating;

    @Column(name = "review_count")
    private Integer reviewCount;

    @ElementCollection
    @CollectionTable(name = "recipe_ingredients", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "ingredient")
    private List<String> ingredients;

    @ElementCollection
    @CollectionTable(name = "recipe_instructions", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "instruction")
    private List<String> instructions;

    @ElementCollection
    @CollectionTable(name = "recipe_tags", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "tag")
    private List<String> tags;

    @ElementCollection
    @CollectionTable(name = "recipe_meal_types", joinColumns = @JoinColumn(name = "recipe_id"))
    @Column(name = "meal_type")
    private List<String> mealType;
}
