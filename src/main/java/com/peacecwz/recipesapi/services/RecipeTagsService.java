package com.peacecwz.recipesapi.services;

import com.peacecwz.recipesapi.data.IngredientEntity;
import com.peacecwz.recipesapi.data.RecipeEntity;
import com.peacecwz.recipesapi.data.TagEntity;
import com.peacecwz.recipesapi.dtos.IngredientDto;
import com.peacecwz.recipesapi.dtos.TagDto;
import com.peacecwz.recipesapi.infrastructure.HttpException;
import com.peacecwz.recipesapi.repositories.RecipeRepository;
import com.peacecwz.recipesapi.repositories.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeTagsService {
    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private TagRepository tagRepository;

    public List<TagDto> getRecipeTags(Long id) {
        return recipeRepository.findById(id).get().getTags().stream()
                .map(TagDto::buildDto)
                .collect(Collectors.toList());
    }

    public void addRecipeTag(Long id, TagDto tag) {
        try {
            Optional<RecipeEntity> recipe = recipeRepository.findById(id);
            if (recipe.isEmpty()) {
                throw new HttpException("Recipe not found", HttpStatus.NOT_FOUND);
            }

            boolean isExistingTag = recipe.get().getTags().stream()
                    .anyMatch(i -> i.getName().equals(tag.getName()));
            if (isExistingTag) {
                throw new HttpException("Tag already exists", HttpStatus.BAD_REQUEST);
            }

            Optional<TagEntity> existingTag = tagRepository.findByName(tag.getName());
            if (existingTag.isPresent()) {
                recipe.get().getTags().add(existingTag.get());
                recipeRepository.save(recipe.get());
                return;
            }

            TagEntity newTag = TagEntity.builder()
                    .name(tag.getName())
                    .build();

            newTag = tagRepository.save(newTag);

            recipe.get().getTags().add(newTag);
            recipeRepository.save(recipe.get());
        } catch (Exception e) {
            throw new HttpException("Failed to add tag", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void updateRecipeTag(Long id, Long tagId, TagDto tag) {
        try {
            Optional<RecipeEntity> recipe = recipeRepository.findById(id);
            if (recipe.isEmpty()) {
                throw new HttpException("Recipe not found", HttpStatus.NOT_FOUND);
            }

            boolean isExistingTag = recipe.get().getTags().stream()
                    .anyMatch(i -> i.getId() == tagId);
            if (!isExistingTag) {
                throw new HttpException("Tag not found", HttpStatus.NOT_FOUND);
            }

            Optional<TagEntity> existingTag = tagRepository.findByName(tag.getName());
            if (existingTag.isPresent()) {
                recipe.get().getTags().removeIf(i -> i.getId() == tagId);
                recipe.get().getTags().add(existingTag.get());
                recipeRepository.save(recipe.get());
                return;
            }

            TagEntity newTag = TagEntity.builder()
                    .name(tag.getName())
                    .build();

            newTag = tagRepository.save(newTag);

            recipe.get().getTags().removeIf(i -> i.getId() == tagId);
            recipe.get().getTags().add(newTag);
            recipeRepository.save(recipe.get());
        } catch (Exception e) {
            throw new HttpException("Failed to update tag", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteRecipeTag(Long id, Long tagId) {
        try {
            Optional<RecipeEntity> recipe = recipeRepository.findById(id);
            if (recipe.isEmpty()) {
                throw new HttpException("Recipe not found", HttpStatus.NOT_FOUND);
            }

            boolean isExistingTag = recipe.get().getTags().stream()
                    .anyMatch(i -> i.getId() == tagId);
            if (!isExistingTag) {
                throw new HttpException("Tag not found", HttpStatus.NOT_FOUND);
            }

            recipe.get().getTags().removeIf(i -> i.getId() == tagId);

            recipeRepository.save(recipe.get());
        } catch (Exception e) {
            throw new HttpException("Failed to delete tag", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
