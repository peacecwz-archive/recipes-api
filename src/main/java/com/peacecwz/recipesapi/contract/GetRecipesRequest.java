package com.peacecwz.recipesapi.contract;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Optional;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetRecipesRequest extends PaginationRequest {
    private String[] tags;
    private String[] includeIngredients;
    private String[] excludeIngredients;
    private Integer servings;
    private String term;
}
