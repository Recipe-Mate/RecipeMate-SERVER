package org.chefcrew.food.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.chefcrew.common.enums.AmountUnit;

@Entity
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Food {
    @Id
    @Column(name = "food_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long foodId;

    private String foodName;

    private float foodAmount;

    @Enumerated(EnumType.STRING)
    private AmountUnit amountUnit;

    private long userId;
    private String imgUrl; //S3작업시 관련 코드 수정 예정

    public Food(String name, float foodAmount, AmountUnit amountUnit, String imgUrl, long userId) {
        this.foodName = name;
        this.foodAmount = foodAmount;
        this.amountUnit = amountUnit;
        this.imgUrl = imgUrl;
        this.userId = userId;
    }

    public void updateAmountAndUnit(float amount, AmountUnit amountUnit) {
        this.foodAmount = amount;
        this.amountUnit = amountUnit;
    }
}
