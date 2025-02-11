package com.ps.recipes.controller;


import com.ps.recipes.dto.RecipeDTO;
import com.ps.recipes.errors.BadRequestAlertException;
import com.ps.recipes.repository.RecipeRepository;
import com.ps.recipes.service.RecipeService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/recipes")
@RequiredArgsConstructor
@Slf4j
public class RecipeController {


        private static final String ENTITY_NAME = "recipe";

        private final RecipeService recipeService;

        private final RecipeRepository recipeRepository;



        /**
         * {@code POST  /recipe} : Create a new customer.
         *
         * @param recipeDTO the customerDTO to create.
         * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new recipeDto, or with status {@code 400 (Bad Request)} if the recipe has already an ID.
         * @throws URISyntaxException if the Location URI syntax is incorrect.
         */
        @PostMapping("")
        public ResponseEntity<RecipeDTO> createCustomer( @RequestBody RecipeDTO recipeDTO) throws URISyntaxException {
            log.debug("REST request to save Customer : {}", recipeDTO);
            if (recipeDTO.id() != null) {
                throw new BadRequestAlertException("A new recipe cannot already have an ID id");
            }
            recipeDTO = recipeService.save(recipeDTO);
            return ResponseEntity.created(new URI("/api/recipe/" + recipeDTO.id()))
                    .body(recipeDTO);
        }

        /**
         * {@code PUT  /recipe/:id} : Updates an existing recipe.
         *
         * @param id the id of the recipeDTO to save.
         * @param recipeDTO the recipeDTO to update.
         * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recipeDTO,
         * or with status {@code 400 (Bad Request)} if the recipeDTO is not valid,
         * or with status {@code 500 (Internal Server Error)} if the recipeDTO couldn't be updated.
         * @throws URISyntaxException if the Location URI syntax is incorrect.
         */
        @PutMapping("/{id}")
        public ResponseEntity<RecipeDTO> updateCustomer(
                @PathVariable(value = "id", required = false) final Long id,
                @RequestBody RecipeDTO recipeDTO
        ) throws URISyntaxException {
            log.debug("REST request to update Customer : {}, {}", id, recipeDTO);
            if (recipeDTO.id() == null) {
                throw new BadRequestAlertException("Invalid id null");
            }
            if (!Objects.equals(id, recipeDTO.id())) {
                throw new BadRequestAlertException("Invalid ID");
            }

            if (!recipeRepository.existsById(id)) {
                throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
            }

            recipeDTO = recipeService.update(recipeDTO);
            return ResponseEntity.ok()
                    .body(recipeDTO);
        }

        /**
         * {@code PATCH  /recipe/:id} : Partial updates given fields of an existing customer, field will ignore if it is null
         *
         * @param id the id of the recipeDTO to save.
         * @param recipeDTO the recipeDTO to update.
         * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated recipeDTO,
         * or with status {@code 400 (Bad Request)} if the recipeDTO is not valid,
         * or with status {@code 404 (Not Found)} if the recipeDTO is not found,
         * or with status {@code 500 (Internal Server Error)} if the recipeDTO couldn't be updated.
         * @throws URISyntaxException if the Location URI syntax is incorrect.
         */
        @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
        public ResponseEntity<RecipeDTO> partialUpdateCustomer(
                @PathVariable(value = "id", required = false) final Long id,
                @RequestBody RecipeDTO recipeDTO
        ) throws URISyntaxException {
            log.debug("REST request to partial update Customer partially : {}, {}", id, recipeDTO);
            if (recipeDTO.id() == null) {
                throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
            }
            if (!Objects.equals(id, recipeDTO.id())) {
                throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
            }

            if (!recipeRepository.existsById(id)) {
                throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
            }

            RecipeDTO result = recipeService.partialUpdate(recipeDTO).orElseThrow(EntityNotFoundException::new);

            return ResponseEntity.ok()
                    .body(result);
        }
//
//        /**
//         * {@code GET  /customers} : get all the customers.
//         *
//         * @param pageable the pagination information.
//         * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of customers in body.
//         */
//        @GetMapping("")
//        public ResponseEntity<List<RecipeDTO>> getAllCustomers(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
//            log.debug("REST request to get a page of Customers");
//            Page<CustomerDTO> page = customerService.findAll(pageable);
//            HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
//            return ResponseEntity.ok().headers(headers).body(page.getContent());
//        }

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

        /**
         * {@code DELETE  /recipe/:id} : delete the "id" recipe.
         *
         * @param id the id of the recipeDTO to delete.
         * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
         */
        @DeleteMapping("/{id}")
        public ResponseEntity<Void> deleteCustomer(@PathVariable("id") Long id) {
            log.debug("REST request to delete Customer : {}", id);
            recipeService.delete(id);
            return ResponseEntity.noContent()
                    .build();
        }


}
