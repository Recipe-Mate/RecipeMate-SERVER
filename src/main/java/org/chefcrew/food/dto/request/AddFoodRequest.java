package org.chefcrew.food.dto.request;

import org.chefcrew.food.dto.NewFoodData;

import java.util.List;

public record AddFoodRequest(
        List<NewFoodData> foodList
) {
}
