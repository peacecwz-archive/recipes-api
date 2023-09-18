package com.peacecwz.recipesapi.data;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "ingredients")
public class IngredientEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ingredients_id_seq")
    private long id;
    private String name;
    private String description;
}
