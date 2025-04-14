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
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long foodId;

    private String foodName;

    private float foodAmount;

    private AmountUnit amountUnit;

    private long userId;

    public Food(String name, float foodAmount, AmountUnit amountUnit, long userId) {
        this.foodName = name;
        this.foodAmount = foodAmount;
        this.amountUnit = amountUnit;
        this.userId = userId;
    }
}
