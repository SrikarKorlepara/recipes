// Commented this because i created api to load the data manually


//package com.ps.recipes.service;
//
//import com.ps.recipes.domain.Recipe;
//import com.ps.recipes.repository.RecipeRepository;
//import com.ps.recipes.service.external.ExternalApiService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.CommandLineRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//@RequiredArgsConstructor
//public class DataLoader implements CommandLineRunner {
//
//    private final RecipeRepository recipeRepository;
//    private final ExternalApiService externalApiService;
//
//    @Override
//    public void run(String... args) {
//        List<Recipe> recipes = externalApiService.fetchRecipes();
//        recipes = recipeRepository.saveAll(recipes);
//        System.out.println("Data successfully ingested into H2 database");
//    }
//}
//
