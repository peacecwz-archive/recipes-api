package com.peacecwz.recipesapi.contract;

import com.peacecwz.recipesapi.dtos.RecipeDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@SuperBuilder
@Data
public class GetRecipesResponse extends PaginationResponse<List<RecipeDto>> {
}
