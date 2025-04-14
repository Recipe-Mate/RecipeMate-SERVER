package org.chefcrew.food.dto.response;

import java.util.List;
import org.chefcrew.food.dto.FoodData;

public record GetOwnFoodResponse(
        List<FoodData> ownFoodList
) {
}
