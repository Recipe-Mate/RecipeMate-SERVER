package org.chefcrew.food.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.chefcrew.common.enums.AmountUnit;

@AllArgsConstructor
@Getter
public class FoodAmount {
    long foodId;
    float amount;
    AmountUnit amountUnit;
}
