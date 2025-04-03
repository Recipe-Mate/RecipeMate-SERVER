package org.chefcrew.recipe.domain;

public record UsedRecipe(
        long recipeId,
        String recipeName,
        String recipeImage
) {
}
