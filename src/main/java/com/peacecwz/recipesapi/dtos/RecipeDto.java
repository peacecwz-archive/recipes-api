package com.peacecwz.recipesapi.dtos;

import com.peacecwz.recipesapi.data.IngredientEntity;
import com.peacecwz.recipesapi.data.RecipeEntity;
import com.peacecwz.recipesapi.data.TagEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDto {
    private long id;
    private String name;
    private String description;
    private List<IngredientDto> ingredients;
    private List<TagDto> tags;
    private int servings;
    private int prepareTime;
    private int cookTime;
    private String instructions;

    public static RecipeDto buildDto(RecipeEntity recipe, Set<IngredientEntity> ingredients, Set<TagEntity> tags) {
        return RecipeDto.builder()
                .id(recipe.getId())
                .name(recipe.getName())
                .description(recipe.getDescription())
                .ingredients(ingredients.stream().map(IngredientDto::buildDto).toList())
                .tags(tags.stream().map(TagDto::buildDto).toList())
                .servings(recipe.getServings())
                .build();
    }
}
