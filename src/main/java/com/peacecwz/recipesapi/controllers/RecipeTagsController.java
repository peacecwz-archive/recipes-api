package com.peacecwz.recipesapi.controllers;

import com.peacecwz.recipesapi.dtos.TagDto;
import com.peacecwz.recipesapi.services.RecipeTagsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/recipes/{id}/tags")
public class RecipeTagsController {
    @Autowired
    private RecipeTagsService recipeTagsService;

    @GetMapping("")
    public List<TagDto> getRecipeTags(@PathVariable Long id) {
        return recipeTagsService.getRecipeTags(id);
    }

    @PostMapping("")
    public void addRecipeTag(@PathVariable Long id, @RequestBody TagDto tag) {
        recipeTagsService.addRecipeTag(id, tag);
    }

    @PutMapping("/{tagId}")
    public void updateRecipeTag(@PathVariable Long id, @PathVariable Long tagId, @RequestBody TagDto tag) {
        recipeTagsService.updateRecipeTag(id, tagId, tag);
    }

    @DeleteMapping("/{tagId}")
    public void deleteRecipeTag(@PathVariable Long id, @PathVariable Long tagId) {
        recipeTagsService.deleteRecipeTag(id, tagId);
    }
}
