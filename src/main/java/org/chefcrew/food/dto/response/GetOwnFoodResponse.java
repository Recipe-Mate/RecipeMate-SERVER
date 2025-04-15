package org.chefcrew.food.dto.response;

import java.util.List;
import org.chefcrew.food.dto.FoodResponseData;

public record GetOwnFoodResponse(
        List<FoodResponseData> ownFoodList
) {
}
