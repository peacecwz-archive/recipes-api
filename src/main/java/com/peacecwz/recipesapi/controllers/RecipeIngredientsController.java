package com.peacecwz.recipesapi.controllers;

import com.peacecwz.recipesapi.dtos.IngredientDto;
import com.peacecwz.recipesapi.services.RecipeIngredientsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/recipes/{id}/ingredients", produces = "application/json")
public class RecipeIngredientsController {

    @Autowired
    private RecipeIngredientsService recipeIngredientsService;

    @GetMapping("")
    public List<IngredientDto> getRecipeIngredients(@PathVariable Long id) {
        return recipeIngredientsService.getRecipeIngredients(id);
    }

    @PostMapping("")
    public void addRecipeIngredient(@PathVariable Long id, @RequestBody IngredientDto ingredient) {
        recipeIngredientsService.addRecipeIngredient(id, ingredient);
    }

    @PutMapping("/{ingredientId}")
    public void updateRecipeIngredient(@PathVariable Long id, @PathVariable Long ingredientId, @RequestBody IngredientDto ingredient) {
        recipeIngredientsService.updateRecipeIngredient(id, ingredientId, ingredient);
    }

    @DeleteMapping("/{ingredientId}")
    public void deleteRecipeIngredient(@PathVariable Long id, @PathVariable Long ingredientId) {
        recipeIngredientsService.deleteRecipeIngredient(id, ingredientId);
    }
}
