package org.chefcrew.food.controller;

import lombok.RequiredArgsConstructor;
import org.chefcrew.jwt.UserId;
import org.chefcrew.food.dto.request.AddFoodRequest;
import org.chefcrew.food.dto.request.DeleteFoodRequest;
import org.chefcrew.food.dto.request.PostAmountUpdateRequest;
import org.chefcrew.food.dto.response.GetOwnFoodResponse;
import org.chefcrew.food.service.FoodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;

    @PostMapping
    public ResponseEntity<Void> saveNewFoodList(
            @UserId Long userId,
            @RequestBody AddFoodRequest requestBody
    ) {
        foodService.saveFoodList(userId, requestBody);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/ownlist")
    private ResponseEntity<GetOwnFoodResponse> getOwnFoodList(
            @UserId Long userId
    ) {
        return ResponseEntity.ok()
                .body(new GetOwnFoodResponse(foodService.getOwnedFoodList(userId)));
    }

    //먹은 양 체크하는 api
    @PostMapping("/amount-update")
    private ResponseEntity<Void> updateFoodAmount(
            @UserId Long userId,
            @RequestBody PostAmountUpdateRequest requestBody
    ){
        foodService.updateFoodAmountAndUnit(userId, requestBody);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping
    private ResponseEntity<Void> deleteUsedFood(
            @UserId Long userId,
            @RequestBody DeleteFoodRequest requestBody) {
        foodService.deleteFood(userId, requestBody);
        return ResponseEntity.ok().build();
    }
}
