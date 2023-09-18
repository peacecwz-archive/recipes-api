package com.peacecwz.recipesapi.data;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;

import java.util.Set;

@Entity(name = "tags")
public class TagEntity {
    @Id
    private long id;
    private String name;

    @ManyToMany
    private Set<RecipeEntity> recipe;
}
