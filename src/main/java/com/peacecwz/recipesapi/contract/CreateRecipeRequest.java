package com.peacecwz.recipesapi.contract;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateRecipeRequest {
    @NotBlank
    @Size(min = 5, max = 70)
    private String name;

    @NotBlank
    @Size(min = 20, max = 150)
    private String description;

    @NotNull
    @Min(1)
    private Integer servings;

    @NotNull
    @Min(1)
    private Integer prepareTime;

    @NotNull
    @Min(1)
    private Integer cookTime;

    @NotBlank
    @Size(min = 70)
    private String instructions;

    private List<String> tags;

    @NotNull
    @Size(min = 1)
    private List<String> ingredients;
}
