package org.chefcrew.food.dto.request;

import java.util.List;

public record AddFoodRequest(
        long userId,
        List<String> foodNameList
) {
}
