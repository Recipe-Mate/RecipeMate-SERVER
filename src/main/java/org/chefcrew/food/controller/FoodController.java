package org.chefcrew.food.controller;

import lombok.RequiredArgsConstructor;
import org.chefcrew.food.dto.request.DeleteFoodRequest;
import org.chefcrew.food.dto.request.AddFoodRequest;
import org.chefcrew.food.dto.response.GetOwnFoodResponse;
import org.chefcrew.food.service.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;

    @PostMapping("/{userId}")
    public ResponseEntity<Void> saveNewFoodList(@PathVariable("userId") long userId, @RequestBody AddFoodRequest requestBody) {
        foodService.saveFoodList(userId, requestBody);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ownlist/{userId}")
    private ResponseEntity<GetOwnFoodResponse> getOwnFoodList(@PathVariable("userId") long userId) {
        return ResponseEntity.ok()
                .body(new GetOwnFoodResponse(foodService.getOwnedFoodList(userId)));
    }

    @DeleteMapping("/{userId}")
    private ResponseEntity<Void> deleteUsedFood(@PathVariable("userId") long userId, @RequestBody DeleteFoodRequest requestBody){
        foodService.deleteFood(userId, requestBody);
        return ResponseEntity.ok().build();
    }
}
