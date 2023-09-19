package com.peacecwz.recipesapi.controllers;

import com.peacecwz.recipesapi.contract.CreateRecipeRequest;
import com.peacecwz.recipesapi.contract.GetRecipesRequest;
import com.peacecwz.recipesapi.contract.GetRecipesResponse;
import com.peacecwz.recipesapi.services.RecipesService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipesController.class)
public class RecipesControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipesService recipesService;

    @Test
    public void it_should_return_recipes() throws Exception {
        GetRecipesResponse mockResponse = new GetRecipesResponse();
        when(recipesService.getRecipes(any(GetRecipesRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(get("/recipes"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(recipesService).getRecipes(any(GetRecipesRequest.class));
    }

    @Test
    public void it_should_create_recipe() throws Exception {
        String requestBody = "{\"name\":\"sampleRecipe\"}";

        mockMvc.perform(post("/recipes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isCreated());

        verify(recipesService).createRecipe(any(CreateRecipeRequest.class));
    }
}