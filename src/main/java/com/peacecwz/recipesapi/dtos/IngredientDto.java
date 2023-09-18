package com.peacecwz.recipesapi.dtos;

import com.peacecwz.recipesapi.data.IngredientEntity;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class IngredientDto {
    private long id;
    private String name;

    public static IngredientDto buildDto(IngredientEntity ingredient) {
        return IngredientDto.builder()
                .id(ingredient.getId())
                .name(ingredient.getName())
                .build();
    }
}
