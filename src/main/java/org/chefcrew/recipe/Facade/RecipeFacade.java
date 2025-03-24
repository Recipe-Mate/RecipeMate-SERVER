package org.chefcrew.recipe.Facade;

import lombok.RequiredArgsConstructor;
import org.chefcrew.common.exception.CustomException;
import org.chefcrew.common.validate.Validation;
import org.chefcrew.recipe.dto.request.PostUsedRecipeRequest;
import org.chefcrew.recipe.entity.OwnRecipe;
import org.chefcrew.recipe.entity.SavedRecipeInfo;
import org.chefcrew.recipe.service.OwnRecipeService;
import org.chefcrew.recipe.service.SavedRecipeInfoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.chefcrew.common.exception.ErrorException.RECIPE_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional
public class RecipeFacade {
    private final SavedRecipeInfoService savedRecipeInfoService;
    private final OwnRecipeService ownRecipeService;

    private final Validation validation;

    //사용한 레시피 추가
    public void postAsUsedRecipe(PostUsedRecipeRequest postUsedRecipeRequest) {
        validation.isExistUserByUserId(postUsedRecipeRequest.userId());
        //db에 저장되 있는 정보인지 확인
        SavedRecipeInfo savedRecipeInfo = savedRecipeInfoService.getMenuDataFromDBByName(postUsedRecipeRequest.recipeName());
        //저장 안된 레시피인 경우, 레시피 저장먼저
        if (savedRecipeInfo == null) {
            //검수 먼저
            if (validation.isExistRecipeByRecipeName(postUsedRecipeRequest.recipeName())) {
                savedRecipeInfo = new SavedRecipeInfo(postUsedRecipeRequest.recipeName(),
                        postUsedRecipeRequest.recipeImage());
                savedRecipeInfoService.postAsUsedRecipe(savedRecipeInfo);
            } else {
                throw new CustomException(RECIPE_NOT_FOUND);
            }
        }
        //소유 관계 저장
        ownRecipeService.saveOwnData(new OwnRecipe(postUsedRecipeRequest.userId(), savedRecipeInfo.getId()));

    }

}
