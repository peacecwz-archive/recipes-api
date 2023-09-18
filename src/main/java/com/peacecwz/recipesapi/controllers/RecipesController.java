package com.peacecwz.recipesapi.controllers;

import com.peacecwz.recipesapi.contract.CreateRecipeRequest;
import com.peacecwz.recipesapi.contract.GetRecipesRequest;
import com.peacecwz.recipesapi.contract.GetRecipesResponse;
import com.peacecwz.recipesapi.services.RecipesService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/recipes")
public class RecipesController {
    private final RecipesService recipesService;

    public RecipesController(RecipesService recipesService) {
        this.recipesService = recipesService;
    }

    @GetMapping("")
    public GetRecipesResponse index(GetRecipesRequest request) {
        return recipesService.getRecipes(request);
    }

    @PostMapping("")
    public void create(@RequestBody CreateRecipeRequest request) {
        recipesService.createRecipe(request);
    }
}
