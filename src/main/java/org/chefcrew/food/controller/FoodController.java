package org.chefcrew.food.controller;

import lombok.RequiredArgsConstructor;
import org.chefcrew.food.dto.request.FoodAddRequest;
import org.chefcrew.food.service.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;

    @PostMapping
    public ResponseEntity<Void> saveNewFoodList(FoodAddRequest requestBody){
        foodService.saveFoodList(requestBody);
        return ResponseEntity.ok().build();
    }
}