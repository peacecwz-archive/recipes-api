package com.peacecwz.recipesapi.controllers;

import com.github.fge.jsonpatch.JsonPatch;
import com.peacecwz.recipesapi.contract.CreateRecipeRequest;
import com.peacecwz.recipesapi.contract.GetRecipesRequest;
import com.peacecwz.recipesapi.contract.GetRecipesResponse;
import com.peacecwz.recipesapi.dtos.RecipeDto;
import com.peacecwz.recipesapi.services.RecipesService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/recipes", produces = "application/json")
public class RecipesController {
    @Autowired
    private RecipesService recipesService;

    @GetMapping("")
    public GetRecipesResponse getRecipes(GetRecipesRequest request) {
        return recipesService.getRecipes(request);
    }

    @GetMapping("/{id}")
    public RecipeDto getRecipe(@PathVariable Long id) {
        return recipesService.getRecipe(id);
    }

    @PostMapping("")
    @ResponseStatus(HttpStatus.CREATED)
    public void createRecipe(@Valid @RequestBody CreateRecipeRequest request) {
        recipesService.createRecipe(request);
    }

    @PatchMapping("/{id}")
    public void updateRecipe(@PathVariable Long id, @RequestBody JsonPatch patchRequest) {
        recipesService.updateRecipe(id, patchRequest);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        recipesService.deleteRecipe(id);
    }
}
