package org.chefcrew.food.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    private long userId;

    public Food(String name, long userId) {
        this.foodName = name;
        this.userId = userId;
    }
}
