package com.peacecwz.recipesapi.data;

import jakarta.persistence.*;
import java.util.Set;

@Entity(name = "recipes")
public class RecipeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "recipes_id_seq")
    private long id;
    private String name;
    private String description;
    private int servings;
    private int prepareTime;
    private int cookTime;

    @ManyToMany
    private Set<TagEntity> tags;

    @ManyToMany
    private Set<IngredientEntity> ingredients;
}
