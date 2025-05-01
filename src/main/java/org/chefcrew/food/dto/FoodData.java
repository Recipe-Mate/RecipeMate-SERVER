package org.chefcrew.food.dto;

import lombok.Getter;
import org.chefcrew.common.enums.AmountUnit;

@Getter
public class FoodData {
    private long foodId;
    private float amount;
    private AmountUnit unit;
}
