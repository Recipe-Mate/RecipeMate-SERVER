package org.chefcrew.common.validate;

import org.chefcrew.recipe.service.RecipeService;
import org.chefcrew.user.repository.UserRepository;

public class Validation {
    private UserRepository userRepository;
    private RecipeService recipeService;

    //openapi에서 레시피id로 조회
    public Boolean isExistRecipe(long recipeId) {
        if (recipeService.getMenuDataFromApiById(recipeId) != null)
            return false;
        else return true;
    }

    public boolean isExistUserByUserId(long userId) {
        return userRepository.findById(userId) != null;
    }
}
