package com.peacecwz.recipesapi.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.fge.jsonpatch.JsonPatch;
import com.peacecwz.recipesapi.contract.CreateRecipeRequest;
import com.peacecwz.recipesapi.contract.GetRecipesRequest;
import com.peacecwz.recipesapi.contract.GetRecipesResponse;
import com.peacecwz.recipesapi.data.IngredientEntity;
import com.peacecwz.recipesapi.data.RecipeEntity;
import com.peacecwz.recipesapi.data.TagEntity;
import com.peacecwz.recipesapi.dtos.RecipeDto;
import com.peacecwz.recipesapi.infrastructure.HttpException;
import com.peacecwz.recipesapi.repositories.IngredientRepository;
import com.peacecwz.recipesapi.repositories.RecipeRepository;
import com.peacecwz.recipesapi.repositories.TagRepository;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RecipesService {
    private final RecipeRepository recipeRepository;
    private final TagRepository tagRepository;
    private final IngredientRepository ingredientRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public RecipesService(RecipeRepository recipeRepository, TagRepository tagRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.tagRepository = tagRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public GetRecipesResponse getRecipes(GetRecipesRequest request) {
        Pageable pageable = PageRequest.of(request.getPage() - 1, request.getSize());

        Specification<RecipeEntity> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (request.getTags() != null) {
                predicates.add(root.join("tags").get("name").in(Arrays.asList(request.getTags())));
            }

            if (request.getIncludeIngredients() != null) {
                predicates.add(root.join("ingredients").get("name").in(Arrays.asList(request.getIncludeIngredients())));
            }

            if (request.getExcludeIngredients() != null) {
                Subquery<RecipeEntity> subquery = query.subquery(RecipeEntity.class);
                Root<RecipeEntity> subqueryRoot = subquery.from(RecipeEntity.class);
                subquery.select(subqueryRoot)
                        .where(subqueryRoot.join("ingredients").get("name").in(Arrays.asList(request.getExcludeIngredients())));
                predicates.add(cb.not(root.in(subquery)));
            }

            if (request.getServings() != null) {
                predicates.add(cb.equal(root.get("servings"), request.getServings()));
            }

            if (request.getTerm() != null) {
                Predicate namePredicate = cb.like(cb.lower(root.get("name")), "%" + request.getTerm().toLowerCase() + "%");
                Predicate descriptionPredicate = cb.like(cb.lower(root.get("description")), "%" + request.getTerm().toLowerCase() + "%");
                Predicate instructionsPredicate = cb.like(cb.lower(root.get("instructions")), "%" + request.getTerm().toLowerCase() + "%");

                Predicate orPredicate = cb.or(namePredicate, descriptionPredicate, instructionsPredicate);
                predicates.add(orPredicate);
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        Page<RecipeEntity> recipePage = recipeRepository.findAll(spec, pageable);

        List<RecipeDto> recipeDtos = recipePage.getContent().stream()
                .map(r -> RecipeDto.buildDto(r, r.getIngredients(), r.getTags()))
                .collect(Collectors.toList());

        return GetRecipesResponse.builder()
                .data(recipeDtos)
                .page(request.getPage())
                .size(request.getSize())
                .total(recipePage.getTotalPages())
                .build();
    }

    public RecipeDto getRecipe(Long id) {
        RecipeEntity recipe = recipeRepository.findById(id).orElseThrow(() -> new HttpException("recipe not found", HttpStatus.NOT_FOUND));
        return RecipeDto.buildDto(recipe, recipe.getIngredients(), recipe.getTags());
    }

    @Transactional
    public void createRecipe(CreateRecipeRequest request) {
        try {
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
        } catch (Exception e) {
            throw new HttpException("failed to create recipe", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void deleteRecipe(Long id) {
        try {
            recipeRepository.deleteById(id);
        } catch (Exception e) {
            throw new HttpException("failed to delete recipe", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public void updateRecipe(Long id, JsonPatch patchRequest) {
        try {
            RecipeEntity recipe = recipeRepository.findById(id).orElseThrow(() -> new HttpException("recipe not found", HttpStatus.NOT_FOUND));
            RecipeDto recipeDto = RecipeDto.buildDto(recipe, recipe.getIngredients(), recipe.getTags());
            JsonNode patched = patchRequest.apply(objectMapper.convertValue(recipeDto, JsonNode.class));
            RecipeDto patchedRecipeDto = objectMapper.treeToValue(patched, RecipeDto.class);

            Set<TagEntity> tags = patchedRecipeDto.getTags().stream()
                    .map(tag -> tagRepository.findByName(tag.getName()).orElseGet(() -> tagRepository.save(TagEntity.builder().name(tag.getName()).build())))
                    .collect(Collectors.toSet());

            Set<IngredientEntity> ingredients = patchedRecipeDto.getIngredients().stream()
                    .map(ingredient -> ingredientRepository.findByName(ingredient.getName()).orElseGet(() -> ingredientRepository.save(IngredientEntity.builder().name(ingredient.getName()).build())))
                    .collect(Collectors.toSet());

            recipe.setName(patchedRecipeDto.getName());
            recipe.setDescription(patchedRecipeDto.getDescription());
            recipe.setServings(patchedRecipeDto.getServings());
            recipe.setPrepareTime(patchedRecipeDto.getPrepareTime());
            recipe.setCookTime(patchedRecipeDto.getCookTime());
            recipe.setInstructions(patchedRecipeDto.getInstructions());
            recipe.setTags(tags);
            recipe.setIngredients(ingredients);

            recipeRepository.save(recipe);
        } catch (Exception e) {
            throw new HttpException("failed to update recipe", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
