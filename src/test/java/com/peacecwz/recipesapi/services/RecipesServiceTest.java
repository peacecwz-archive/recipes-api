package com.peacecwz.recipesapi.services;

import com.peacecwz.recipesapi.contract.GetRecipesRequest;
import com.peacecwz.recipesapi.contract.GetRecipesResponse;
import com.peacecwz.recipesapi.data.IngredientEntity;
import com.peacecwz.recipesapi.data.RecipeEntity;
import com.peacecwz.recipesapi.data.TagEntity;
import com.peacecwz.recipesapi.dtos.TagDto;
import com.peacecwz.recipesapi.repositories.IngredientRepository;
import com.peacecwz.recipesapi.repositories.RecipeRepository;
import com.peacecwz.recipesapi.repositories.TagRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

public class RecipesServiceTest {
    @InjectMocks
    private RecipesService recipesService;

    @Mock
    private RecipeRepository recipeRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private IngredientRepository ingredientRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void it_should_return_empty_recipes() {

        GetRecipesRequest request = new GetRecipesRequest();
        request.setPage(1);
        request.setSize(10);
        Page<RecipeEntity> result = new PageImpl<>(new ArrayList<>());
        when(recipeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(result);

        GetRecipesResponse response = recipesService.getRecipes(request);

        assertEquals(10, response.getSize());
        assertEquals(1, response.getPage());
    }

    @Test
    public void it_should_return_recipes() {

        List<RecipeEntity> recipes = new ArrayList<>();
        RecipeEntity recipe = RecipeEntity.builder()
                .ingredients(new HashSet<>())
                .tags(new HashSet<>())
                .build();
        recipes.add(recipe);

        GetRecipesRequest request = new GetRecipesRequest();
        request.setPage(1);
        request.setSize(10);
        Page<RecipeEntity> result = new PageImpl<>(recipes);
        when(recipeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(result);

        GetRecipesResponse response = recipesService.getRecipes(request);

        assertEquals(10, response.getSize());
        assertEquals(1, response.getPage());
        assertEquals(1, response.getData().size());
    }

    @Test
    public void it_should_filter_by_single_tag() {
        GetRecipesRequest request = new GetRecipesRequest();
        request.setPage(1);
        request.setSize(10);
        request.setTags(new String[]{"Vegetarian"});
        Set<TagEntity> tags = new HashSet<>();
        tags.add(TagEntity.builder().name("Vegetarian").build());
        List<RecipeEntity> recipes = new ArrayList<>();
        RecipeEntity recipe = RecipeEntity.builder()
                .ingredients(new HashSet<>())
                .tags(tags)
                .build();
        recipes.add(recipe);
        Page<RecipeEntity> result = new PageImpl<>(recipes);

        when(recipeRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(result);

        GetRecipesResponse response = recipesService.getRecipes(request);

        assertTrue(response.getData().stream().allMatch(r -> r.getTags().contains(TagDto.builder().name("Vegetarian").build())));
    }
}