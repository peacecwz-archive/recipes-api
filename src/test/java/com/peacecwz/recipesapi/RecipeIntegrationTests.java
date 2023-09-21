package com.peacecwz.recipesapi;

import com.peacecwz.recipesapi.contract.CreateRecipeRequest;
import com.peacecwz.recipesapi.repositories.RecipeRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@Testcontainers
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecipeIntegrationTests {
    @LocalServerPort
    private Integer port;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RecipeRepository recipeRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Container
    private static final PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("recipes-db").withUsername("username").withPassword("password");

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.datasource.url", postgreSQLContainer::getJdbcUrl);
        dynamicPropertyRegistry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        dynamicPropertyRegistry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        dynamicPropertyRegistry.add("spring.jpa.database-platform", () -> "org.hibernate.dialect.PostgreSQLDialect");
        dynamicPropertyRegistry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    static {
        postgreSQLContainer.start();
    }

    @BeforeAll
    static void beforeAll() {
        postgreSQLContainer.start();
    }

    @AfterAll
    static void afterAll() {
        postgreSQLContainer.stop();
    }

    @Test
    @Order(value = 1)
    public void it_should_ensure_initialize_repositories() {
        assertNotNull(mockMvc);
        assertNotNull(recipeRepository);
    }

    @Test
    @Order(value = 2)
    public void it_should_return_empty_recipes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/recipes")).andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(0));
    }

    @Test
    @Order(value = 3)
    public void it_should_create_recipe() throws Exception {
        CreateRecipeRequest request = CreateRecipeRequest.builder()
                .name("Adana Kebab")
                .description("The best kebab in the world. Adana Kebab is more spicy than other kebabs.")
                .ingredients(List.of(new String[]{"ground beef", "onions", "spices"}))
                .tags(List.of(new String[]{"kebab", "spicy", "meat"}))
                .cookTime(10)
                .instructions("Prepare ground beef, onions and spices. Mix them and put them on the skewers. Cook them on the grill.")
                .prepareTime(10)
                .servings(1)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/recipes")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @Order(value = 4)
    public void it_should_return_recipes() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/recipes")).andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(1));
    }
}
