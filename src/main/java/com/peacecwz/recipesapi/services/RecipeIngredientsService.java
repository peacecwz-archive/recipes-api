package com.peacecwz.recipesapi.services;

import com.peacecwz.recipesapi.data.IngredientEntity;
import com.peacecwz.recipesapi.data.RecipeEntity;
import com.peacecwz.recipesapi.dtos.IngredientDto;
import com.peacecwz.recipesapi.infrastructure.HttpException;
import com.peacecwz.recipesapi.repositories.IngredientRepository;
import com.peacecwz.recipesapi.repositories.RecipeRepository;
import jakarta.transaction.Transactional;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecipeIngredientsService {
    private final Logger logger = LogManager.getLogger(RecipeIngredientsService.class);

    @Autowired
    private RecipeRepository recipeRepository;

    @Autowired
    private IngredientRepository ingredientRepository;

    public List<IngredientDto> getRecipeIngredients(Long id) {
        return recipeRepository.findById(id).get().getIngredients().stream()
                .map(IngredientDto::buildDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void addRecipeIngredient(Long recipeId, IngredientDto ingredient) {
        try {
            Optional<RecipeEntity> recipe = recipeRepository.findById(recipeId);
            if (recipe.isEmpty()) {
                throw new HttpException("Recipe not found", HttpStatus.NOT_FOUND);
            }

            boolean isExistingIngredient = recipe.get().getIngredients().stream()
                    .anyMatch(i -> i.getName().equals(ingredient.getName()));
            if (isExistingIngredient) {
                throw new HttpException("Ingredient already exists", HttpStatus.BAD_REQUEST);
            }

            Optional<IngredientEntity> existingIngredient = ingredientRepository.findByName(ingredient.getName());
            if (existingIngredient.isPresent()) {
                recipe.get().getIngredients().add(existingIngredient.get());
                recipeRepository.save(recipe.get());
                return;
            }

            IngredientEntity newIngredient = IngredientEntity.builder()
                    .name(ingredient.getName())
                    .build();

            newIngredient = ingredientRepository.save(newIngredient);

            recipe.get().getIngredients().add(newIngredient);
            recipeRepository.save(recipe.get());
        } catch (Exception e) {
            logger.error(e);
            throw new HttpException("Failed to add ingredient", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void updateRecipeIngredient(Long id, Long ingredientId, IngredientDto ingredient) {
        try {
            Optional<RecipeEntity> recipe = recipeRepository.findById(id);
            if (recipe.isEmpty()) {
                throw new HttpException("Recipe not found", HttpStatus.NOT_FOUND);
            }

            boolean isExistingIngredient = recipe.get().getIngredients().stream()
                    .anyMatch(i -> i.getId() == ingredientId);
            if (!isExistingIngredient) {
                throw new HttpException("Ingredient not found", HttpStatus.NOT_FOUND);
            }

            Optional<IngredientEntity> existingIngredient = ingredientRepository.findByName(ingredient.getName());
            if (existingIngredient.isPresent()) {
                recipe.get().getIngredients().removeIf(i -> i.getId() == ingredientId);
                recipe.get().getIngredients().add(existingIngredient.get());
                recipeRepository.save(recipe.get());
                return;
            }

            IngredientEntity newIngredient = IngredientEntity.builder()
                    .name(ingredient.getName())
                    .build();

            newIngredient = ingredientRepository.save(newIngredient);

            recipe.get().getIngredients().removeIf(i -> i.getId() == ingredientId);
            recipe.get().getIngredients().add(newIngredient);
            recipeRepository.save(recipe.get());
        } catch (Exception e) {
            logger.error(e);
            throw new HttpException("Failed to update ingredient", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteRecipeIngredient(Long recipeId, Long ingredientId) {
        try {
            Optional<RecipeEntity> recipe = recipeRepository.findById(recipeId);
            if (recipe.isEmpty()) {
                throw new HttpException("Recipe not found", HttpStatus.NOT_FOUND);
            }

            boolean isExistingIngredient = recipe.get().getIngredients().stream()
                    .anyMatch(i -> i.getId() == ingredientId);
            if (!isExistingIngredient) {
                throw new HttpException("Ingredient not found", HttpStatus.NOT_FOUND);
            }

            recipe.get().getIngredients().removeIf(i -> i.getId() == ingredientId);

            recipeRepository.save(recipe.get());
        } catch (Exception e) {
            logger.error(e);
            throw new HttpException("Failed to delete ingredient", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
