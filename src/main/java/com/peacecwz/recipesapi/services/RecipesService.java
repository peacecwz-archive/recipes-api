package com.peacecwz.recipesapi.services;

import com.peacecwz.recipesapi.contract.CreateRecipeRequest;
import com.peacecwz.recipesapi.contract.GetRecipesRequest;
import com.peacecwz.recipesapi.contract.GetRecipesResponse;
import com.peacecwz.recipesapi.data.IngredientEntity;
import com.peacecwz.recipesapi.data.RecipeEntity;
import com.peacecwz.recipesapi.data.TagEntity;
import com.peacecwz.recipesapi.dtos.RecipeDto;
import com.peacecwz.recipesapi.repositories.IngredientRepository;
import com.peacecwz.recipesapi.repositories.RecipeRepository;
import com.peacecwz.recipesapi.repositories.RecipeSpecifications;
import com.peacecwz.recipesapi.repositories.TagRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecipesService {
    private final RecipeRepository recipeRepository;
    private final TagRepository tagRepository;
    private final IngredientRepository ingredientRepository;

    public RecipesService(RecipeRepository recipeRepository, TagRepository tagRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.tagRepository = tagRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public GetRecipesResponse getRecipes(GetRecipesRequest request) {
        return null;
    }

    public boolean createRecipe(CreateRecipeRequest request) {
        Set<TagEntity> tags = request.getTags().stream()
                .map(tagName -> tagRepository.findByName(tagName).orElseGet(() -> tagRepository.save(TagEntity.builder().name(tagName).build())))
                .collect(Collectors.toSet());

        Set<IngredientEntity> ingredients = request.getIngredients().stream()
                .map(ingredientName -> ingredientRepository.findByName(ingredientName).orElseGet(() -> ingredientRepository.save(IngredientEntity.builder().name(ingredientName).build())))
                .collect(Collectors.toSet());

        RecipeEntity recipe = RecipeEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .servings(request.getServings())
                .prepareTime(request.getPrepareTime())
                .cookTime(request.getCookTime())
                .instructions(request.getInstructions())
                .tags(tags)
                .ingredients(ingredients)
                .build();

        recipeRepository.save(recipe);

        return true;
    }
}
