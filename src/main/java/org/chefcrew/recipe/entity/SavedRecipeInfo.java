package org.chefcrew.recipe.entity;


public record SavedRecipeInfo(
        long id,
        long recipeId,
        String recipeName,
        String recipeImage
) {

}
