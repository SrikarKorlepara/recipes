package com.ps.recipes.dto;

import com.ps.recipes.domain.Recipe;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class LoaderResponseDTO{
    private List<Recipe> recipes;
    private Integer total;
    private Integer skip;
    private Integer limit;

}
