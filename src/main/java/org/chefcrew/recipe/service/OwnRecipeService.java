package org.chefcrew.recipe.service;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.chefcrew.recipe.entity.OwnRecipe;
import org.chefcrew.recipe.repository.OwnRecipeRepository;
import org.chefcrew.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class OwnRecipeService {
    private final OwnRecipeRepository ownRecipeRepository;

    //유저 id로 조회하는 메서드
    //아이디로 저장한 레시피 리스트 검색
    public List<OwnRecipe> getOwnRecipeListByUserId(long userId) {
        return ownRecipeRepository.findAllByUserId(userId);
    }

    //저장
    public void saveOwnData(OwnRecipe ownRecipe) {
        ownRecipeRepository.saveOwnData(ownRecipe);
    }

    public void deleteAllByUser(User user) {
        List<OwnRecipe> foods = ownRecipeRepository.findAllByUserId(user.getUserId());
        ownRecipeRepository.deleteAllById(foods.stream().map(
                (OwnRecipe::getId)).collect(Collectors.toList()));
    }
}
