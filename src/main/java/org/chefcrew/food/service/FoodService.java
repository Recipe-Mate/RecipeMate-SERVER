package org.chefcrew.food.service;

import static org.chefcrew.common.exception.ErrorException.USER_NOT_FOUND;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.chefcrew.common.exception.CustomException;
import org.chefcrew.food.dto.request.AddFoodRequest;
import org.chefcrew.food.dto.request.DeleteFoodRequest;
import org.chefcrew.food.entity.Food;
import org.chefcrew.food.repository.FoodRepository;
import org.chefcrew.user.entity.User;
import org.chefcrew.user.service.UserService;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class FoodService {
    public final FoodRepository foodRepository;
    public final UserService userService;

    @Transactional
    public void saveFoodList(long userId, AddFoodRequest foodAddRequest) {
        validateUser(userId);
        List<Food> foodDataList = foodAddRequest.foodNameList().stream()
                .filter(name -> !foodRepository.existsByFoodNameAndUserId(name, userId))
                .map(name -> new Food(name, userId))
                .toList();
        if (foodDataList != null) {
            foodDataList.forEach(foodRepository::saveFood);
        }
    }

    private void validateUser(long userId) {
        if (userService.getUserInfo(userId) == null) {
            throw new CustomException(USER_NOT_FOUND);
        }
    }

    public List<String> getOwnedFoodList(long userId) {
        validateUser(userId);
        if (!foodRepository.existsByUserId(userId)) {
            return null;
        }
        List<Food> foodList = foodRepository.findByUserId(userId);
        if (foodList != null) {
            return foodList.stream().map(food -> food.getFoodName())
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    public void deleteFood(long userId, DeleteFoodRequest deleteFoodRequest) {
        foodRepository.deleteFood(userId, deleteFoodRequest.foodNameList());
    }

    public void deleteAllByUser(User user) throws IOException {
        List<Food> foods = foodRepository.getAllByUser(user);
        foodRepository.deleteAllById(foods.stream().map(
                (Food::getFoodId)).collect(Collectors.toList()));
    }
}
