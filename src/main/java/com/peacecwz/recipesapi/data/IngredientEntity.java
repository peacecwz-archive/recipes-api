package com.peacecwz.recipesapi.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.Set;

@Entity(name = "ingredients")
public class IngredientEntity {
    @Id
    private long id;
    private String name;
    private String description;

    @ManyToMany
    private Set<RecipeEntity> recipe;
}
