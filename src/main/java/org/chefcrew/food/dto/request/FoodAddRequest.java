package org.chefcrew.food.dto.request;

import java.util.List;

public record FoodAddRequest(
        long userId,
        List<String> foodNameList
) {
}
