package org.chefcrew.food.service;

import static org.chefcrew.common.exception.ErrorException.USER_NOT_FOUND;

import jakarta.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.chefcrew.common.exception.CustomException;
import org.chefcrew.food.dto.request.AddFoodRequest;
import org.chefcrew.food.dto.request.DeleteFoodRequest;
import org.chefcrew.food.entity.Food;
import org.chefcrew.food.repository.FoodRepository;
import org.chefcrew.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class FoodService {
    public final FoodRepository foodRepository;
    public final UserService userService;

    @Transactional
    public void saveFoodList(AddFoodRequest foodAddRequest) {
        validateUser(foodAddRequest.userId());
        List<Food> foodDataList = foodAddRequest.foodNameList().stream()
                .filter(name -> !foodRepository.existsByFoodNameAndUserId(name, foodAddRequest.userId()))
                .map(name -> new Food(name, foodAddRequest.userId()))
                .toList();
        foodDataList.forEach(foodRepository::saveFood);
    }

    private void validateUser(long userId) {
        if (userService.getUserInfo(userId) == null) {
            throw new CustomException(USER_NOT_FOUND);
        }
    }

    public List<String> getOwnedFoodList(long userId) {
        validateUser(userId);
        List<Food> foodList = foodRepository.findByUserId(userId);
        if (foodList != null) {
            return foodList.stream().map(food -> food.getFoodName())
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    public void deleteFood(DeleteFoodRequest deleteFoodRequest) {
        foodRepository.deleteFood(deleteFoodRequest.foodNameList());
    }
}
