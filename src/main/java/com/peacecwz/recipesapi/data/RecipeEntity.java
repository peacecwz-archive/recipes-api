package com.peacecwz.recipesapi.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
    private String instructions;

    @ManyToMany
    @JoinTable(
            name = "recipe_tags",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )
    private Set<TagEntity> tags;

    @ManyToMany
    @JoinTable(
            name = "recipe_ingredients",
            joinColumns = @JoinColumn(name = "recipe_id"),
            inverseJoinColumns = @JoinColumn(name = "ingredient_id")
    )
    private Set<IngredientEntity> ingredients;
}
