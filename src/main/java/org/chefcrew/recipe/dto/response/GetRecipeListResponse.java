package org.chefcrew.recipe.dto.response;

import java.util.List;
import org.chefcrew.recipe.domain.Recipe;

public record GetRecipeListResponse(
        List<Recipe> recipeList
) {
}
