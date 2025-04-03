package org.chefcrew.recipe.dto.request;

public record PostUsedRecipeRequest(
        String recipeName,
        String recipeImage
) {
}
