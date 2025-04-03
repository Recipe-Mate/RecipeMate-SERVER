package org.chefcrew.recipe.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.chefcrew.recipe.entity.SavedRecipeInfo;
import org.chefcrew.recipe.repository.SavedRecipeInfoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
//레시피 사용한거 저장/레시피id로 조회(있는지)
public class SavedRecipeInfoService {
    public final SavedRecipeInfoRepository savedRecipeInfoRepository;

    public SavedRecipeInfo getMenuDataFromDBByName(String recipeName) {
        if (savedRecipeInfoRepository.existsByRecipeName(recipeName))
            return savedRecipeInfoRepository.findByRecipeName(recipeName);
        return null;
    }

    //저장하는 메서드
    //요청받은 레시피 사용한 레시피 리스트에 추가\
    public void postAsUsedRecipe(SavedRecipeInfo savedRecipeInfo) {
        //레십 정보를 저장하는 과정
        savedRecipeInfoRepository.saveRecipe(savedRecipeInfo);
    }

    //조회하는 메서드
    public List<SavedRecipeInfo> getRecipeInfoList(List<Long> recipesIdList) {
        return recipesIdList.stream()
                .map(tagId -> savedRecipeInfoRepository.findByRecipeId(tagId))
                .toList();
    }

}
