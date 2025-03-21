package org.chefcrew.recipe.dto.request;

public record PostUsedRecipeRequest(
        long userId,
        String recipeName,
        String recipeImage
) {
}
