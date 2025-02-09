package org.chefcrew.recipe.dto.request;

public record PostUsedRecipeRequest(
        long recipeId,
        long userId
) {
}
