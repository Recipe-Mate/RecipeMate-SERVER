package org.chefcrew.food.service;

import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.chefcrew.food.dto.request.FoodAddRequest;
import org.chefcrew.food.entity.Food;
import org.chefcrew.food.repository.FoodRepository;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class FoodService {
    public final FoodRepository foodRepository;

    public void saveFoodList(FoodAddRequest foodAddRequest) {
        List<Food> foodDataList = foodAddRequest.foodNameList().stream()
                .map(name -> new Food(name, foodAddRequest.userId()))
                .toList();
        foodRepository.saveFoodList(foodDataList);
    }
}
