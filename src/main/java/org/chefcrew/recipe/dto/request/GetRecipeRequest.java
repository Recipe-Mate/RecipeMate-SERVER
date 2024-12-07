package org.chefcrew.recipe.dto.request;

import org.chefcrew.recipe.enums.ValueOption;

public record GetRecipeRequest(
        String foodName,
        ValueOption calorie,        //칼로리
        ValueOption fat,            //지방
        ValueOption sodium          //나트륨
) {
}
