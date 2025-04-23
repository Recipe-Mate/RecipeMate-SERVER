package org.chefcrew.common.validate;

import org.chefcrew.recipe.service.RecipeService;
import org.chefcrew.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class Validation {
    private final UserRepository userRepository;
    private RecipeService recipeService;

    public Validation(UserRepository userRepository, RecipeService recipeService) {
        this.userRepository = userRepository;
        this.recipeService = recipeService;
    }

    //openapi에서 레시피명으로 조회
    public Boolean isExistRecipeByRecipeName(String recipeName) {
        if (recipeService.getMenuDataFromApiByRecipeName(recipeName) != null)
            return true;
        return false;
    }

    public boolean isExistUserByUserId(long userId) {
        return userRepository.findById(userId) != null;
    }
}
