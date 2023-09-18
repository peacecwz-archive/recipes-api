package com.peacecwz.recipesapi.repositories;

import com.peacecwz.recipesapi.data.RecipeEntity;
import org.springframework.data.jpa.domain.Specification;

public class RecipeSpecifications {

    public static Specification<RecipeEntity> hasTag(Long tag) {
        return (root, query, criteriaBuilder) -> {
            if(tag == null) return null;
            return criteriaBuilder.isMember(tag, root.get("tags"));
        };
    }

    public static Specification<RecipeEntity> hasIngredient(Long ingredient) {
        return (root, query, criteriaBuilder) -> {
            if(ingredient == null) return null;
            return criteriaBuilder.isMember(ingredient, root.get("ingredients"));
        };
    }

    public static Specification<RecipeEntity> hasServings(Integer serving) {
        return (root, query, criteriaBuilder) -> {
            if(serving == null) return null;
            return criteriaBuilder.equal(root.get("servings"), serving);
        };
    }
}