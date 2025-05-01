package org.chefcrew.recipe.domain;

import java.util.List;

public record Recipe(
        String recipeName,
        String dishImg,
        List<String> ingredient,
        List<String> cookingProcess,
        List<String> processImage,
        Float calorie,
        Float natrium,
        Float fat,
        Float protien,
        Float carbohydrate
) {
}
