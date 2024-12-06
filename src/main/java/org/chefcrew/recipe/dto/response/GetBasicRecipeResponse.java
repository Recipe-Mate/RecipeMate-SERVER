package org.chefcrew.recipe.dto.response;

import java.util.List;
import org.chefcrew.recipe.entity.BasicRecipeInfo;
import org.chefcrew.recipe.entity.Result;

public record GetBasicRecipeResponse (
        Integer totalCnt,
        Integer startRow,
        Integer endRow,
        Result result,
        List<BasicRecipeInfo> row
){
}
