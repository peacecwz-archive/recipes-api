package com.peacecwz.recipesapi.contract;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateRecipeRequest {
    private String name;
    private String description;
    private int servings;
    private int prepareTime;
    private int cookTime;
    private String instructions;
    private List<String> tags;
    private List<String> ingredients;
}
