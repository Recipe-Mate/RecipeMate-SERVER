package org.chefcrew.recipe.service;

import lombok.RequiredArgsConstructor;
import org.chefcrew.recipe.entity.OwnRecipe;
import org.chefcrew.recipe.repository.OwnRecipeRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OwnRecipeService {
    private final OwnRecipeRepository ownRecipeRepository;
    //유저 id로 조회하는 메서드
    //아이디로 저장한 레시피 리스트 검색
    public List<OwnRecipe> getOwnRecipeListByUserId(long userId) {
        return ownRecipeRepository.findByUserId(userId);
    }

    //저장
    public void saveOwnData(OwnRecipe ownRecipe){
        ownRecipeRepository.saveOwnData(ownRecipe);
    }
}
