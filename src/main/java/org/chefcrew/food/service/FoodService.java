package org.chefcrew.food.service;

import static org.chefcrew.common.exception.ErrorException.USER_NOT_FOUND;

import jakarta.transaction.Transactional;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.chefcrew.common.exception.CustomException;
import org.chefcrew.food.domain.FoodAmount;
import org.chefcrew.food.dto.FoodData;
import org.chefcrew.food.dto.FoodResponseData;
import org.chefcrew.food.dto.request.AddFoodRequest;
import org.chefcrew.food.dto.request.DeleteFoodRequest;
import org.chefcrew.food.dto.request.PostAmountUpdateRequest;
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

        //신규 등장한 음식 데이터 저장
        List<Food> foodList = foodAddRequest.foodList().stream()
                .map(food -> new Food(food.getFoodName(), food.getAmount(), food.getUnit(), null,
                        userId)) //TODO 이미지 관련 작업 진행 예정
                .toList();

        if (foodList != null) {
            foodList.forEach(foodRepository::saveFood);
        }
    }

    private void validateUser(long userId) {
        if (userService.getUserInfo(userId) == null) {
            throw new CustomException(USER_NOT_FOUND);
        }
    }

    public List<FoodResponseData> getOwnedFoodList(long userId) {
        validateUser(userId);
        if (!foodRepository.existsByUserId(userId)) {
            return null;
        }
        List<Food> foodList = foodRepository.findByUserId(userId);
        if (foodList != null) {
            return foodList.stream()
                    .map(food -> new FoodResponseData(food.getFoodId(), food.getFoodName(), food.getFoodAmount(),
                            food.getAmountUnit(), food.getImgUrl()))
                    .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    public void deleteFood(long userId, DeleteFoodRequest deleteFoodRequest) {
        foodRepository.deleteFood(userId, deleteFoodRequest.foodIdList());
    }

    public void deleteAllByUser(User user) throws IOException {
        List<Food> foods = foodRepository.getAllByUser(user);
        foodRepository.deleteAllById(foods.stream().map(
                (Food::getFoodId)).collect(Collectors.toList()));
    }

    @Transactional
    public void updateFoodAmountAndUnit(long userId, PostAmountUpdateRequest postAmountUpdateRequest){
        List<FoodData> foodDataList = postAmountUpdateRequest.foodDataList();
        List<FoodAmount> foodAmountList = getFoodAmountList(foodDataList);
        List<Food> originDataList = foodRepository.getByUserIdAndFoodId(userId, foodAmountList);

        Map<Long, Food> foodMap = originDataList.stream()
                .collect(Collectors.toMap(Food::getFoodId, Function.identity()));

        for(FoodAmount requestData: foodAmountList) {
            Food food = foodMap.get(requestData.getFoodId());

            if (food != null) {
                food.updateAmountAndUnit(requestData.getAmount(), requestData.getAmountUnit());
            }
        }
    }

    public List<FoodAmount> getFoodAmountList(List<FoodData> foodDataList){
        return foodDataList.stream().map(foodData -> new FoodAmount(foodData.getFoodId(), foodData.getAmount(), foodData.getUnit()))
                .collect(Collectors.toList());
    }
}
