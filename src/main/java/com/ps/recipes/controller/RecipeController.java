package com.ps.recipes.controller;


import com.ps.recipes.dto.ErrorResponseDTO;
import com.ps.recipes.dto.RecipeDTO;
import com.ps.recipes.repository.RecipeRepository;
import com.ps.recipes.service.RecipeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "http://localhost:3000")
public class RecipeController {


        private static final String ENTITY_NAME = "recipe";

        private final RecipeService recipeService;

        private final RecipeRepository recipeRepository;


        /**
         * {@code GET  /recipe/:id} : get the "id" recipeDTO.
         *
         * @param id the id of the recipeDTO to retrieve.
         * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the recipeDTO, or with status {@code 404 (Not Found)}.
         */
        @GetMapping("/{id}")
        public ResponseEntity<RecipeDTO> getCustomer(@PathVariable("id") Long id) {
            log.debug("REST request to get Customer : {}", id);
            Optional<RecipeDTO> customerDTO = recipeService.findOne(id);
            return ResponseEntity.ok()
                    .body(customerDTO.orElseThrow(EntityNotFoundException::new));
        }


        @GetMapping("/search")
        public ResponseEntity<List<RecipeDTO>> search(@RequestParam String query) {
            log.debug("REST request to search for a page of Recipes for query {}", query);
            return ResponseEntity.ok()
                    .body(recipeService.searchByText(query));
        }

        /**
         * {@code GET  /recipes} : get all the recipes.
         *
         * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of recipes in body.
         */
        @GetMapping("findAll")
        public ResponseEntity<List<RecipeDTO>> getAllRecipes() {
            log.debug("REST request to get a page of Recipes");
            return ResponseEntity.ok()
                    .body(recipeService.findAll());
        }

        @PostMapping("/loadAll")
        public ResponseEntity<List<RecipeDTO>> loadRecipes() {
            log.debug("REST request to load all recipes");
            return ResponseEntity.ok()
                    .body(recipeService.loadAll());
        }


}


























