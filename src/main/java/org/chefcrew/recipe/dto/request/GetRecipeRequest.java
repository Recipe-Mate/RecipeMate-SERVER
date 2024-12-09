package org.chefcrew.recipe.dto.request;

import org.chefcrew.recipe.enums.ValueOption;

public record GetRecipeRequest(
        String foodName,
        ValueOption calorie,        //칼로리
        ValueOption fat,            //지방
        ValueOption natrium,          //나트륨
        ValueOption protien,            //단백질
        ValueOption carbohydrate        //탄수화물
) {
}
