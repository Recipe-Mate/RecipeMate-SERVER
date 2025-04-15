package org.chefcrew.food.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.chefcrew.common.enums.AmountUnit;

@Getter
@AllArgsConstructor
public class FoodResponseData {
    private long foodId;
    private String foodName;
    private float amount;
    private AmountUnit unit;
    private String imgUrl;
}
