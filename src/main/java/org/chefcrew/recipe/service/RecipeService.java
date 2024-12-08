package org.chefcrew.recipe.service;

import jakarta.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.chefcrew.recipe.domain.Recipe;
import org.chefcrew.recipe.dto.request.GetRecipeRequest;
import org.chefcrew.recipe.dto.response.GetRecipeOpenResponse;
import org.chefcrew.recipe.dto.response.GetRecipeOpenResponse.RecipeData;
import org.chefcrew.recipe.dto.response.GetRecipeResponse;
import org.chefcrew.recipe.enums.ValueOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Transactional
@RequiredArgsConstructor
public class RecipeService {

    @Value("${apiKey}")
    private String apiKey;

    public GetRecipeResponse getRecommendRecipe(GetRecipeRequest getRecipeRequest) {

        Boolean calorieHigh = null;
        Boolean natriumHigh = null;
        Boolean fatHigh = null;
        Boolean protienHigh = null;
        Boolean carbohydrateHigh = null;

        //공공데이터에 메뉴 조회
        GetRecipeOpenResponse getRecipeOpenResponse = getMenuDataFromApi(getRecipeRequest.foodName());
        //메뉴 필터링
        calorieHigh = getRecipeRequest.calorie() == ValueOption.NONE ? null
                : (getRecipeRequest.calorie() == ValueOption.HIGH ? true : false);        //700kcal 이상
        fatHigh = getRecipeRequest.fat() == ValueOption.NONE ? null
                : (getRecipeRequest.fat() == ValueOption.HIGH ? true : false);         //지방

        natriumHigh = getRecipeRequest.natrium() == ValueOption.NONE ? null
                : (getRecipeRequest.natrium() == ValueOption.HIGH ? true : false);         //나트륨
        protienHigh = getRecipeRequest.protien() == ValueOption.NONE ? null
                : (getRecipeRequest.protien() == ValueOption.HIGH ? true : false);         //단백질
        carbohydrateHigh = getRecipeRequest.carbohydrate() == ValueOption.NONE ? null
                : (getRecipeRequest.carbohydrate() == ValueOption.HIGH ? true : false);         //탄수화물

        //mapping해서 전달
        Boolean finalCalorieHigh = calorieHigh;
        Boolean finalFatHigh = fatHigh;
        Boolean finalNatriumHigh = natriumHigh;
        Boolean finalProtienHigh = protienHigh;
        Boolean finalCarbohydrateHigh = carbohydrateHigh;
        List<RecipeData> recipeResponseList = getRecipeOpenResponse.cookRcpInfo().row()
                .stream()
                .filter(recipeData -> (isAppriateRecipe(finalCalorieHigh, finalFatHigh, finalNatriumHigh,
                        finalProtienHigh,
                        finalCarbohydrateHigh, recipeData)))
                .collect(Collectors.toList());

        List<Recipe> recipeList = recipeResponseList.stream()
                .map(recipeData -> new Recipe(recipeData.recipeName(),
                        Arrays.stream(recipeData.partsDetails().split("\n|, |,"))
                                .map(food -> food.contains(":")? food.split(": ")[1]: food)
                                .toList(),
                        recipeData.getManuals(),
                        recipeData.getManualImages(),
                        recipeData.infoCal(),
                        recipeData.infoNa(),
                        recipeData.infoFat(),
                        recipeData.infoPro(),
                        recipeData.infoCar()))
                .collect(Collectors.toList());

        return new GetRecipeResponse(recipeList);
    }

    private boolean isAppriateRecipe(Boolean finalCalorieHigh, Boolean finalFatHigh, Boolean finalNatriumHigh,
                                     Boolean finalProtienHigh, Boolean finalCarbohydrateHigh, RecipeData recipeData) {
        return (finalCalorieHigh == null || finalCalorieHigh == isCalorieHigh(recipeData.infoCal()))
                && (finalFatHigh == null || finalFatHigh == isFatHigh(recipeData.infoFat()))
                && (finalNatriumHigh == null || finalNatriumHigh == isNatriumHigh(recipeData.infoNa()))
                && (finalProtienHigh == null || finalProtienHigh == isProtienHigh(recipeData.infoPro()))
                && (finalCarbohydrateHigh == null || finalCarbohydrateHigh == isCarbohydrateHigh(recipeData.infoCar()));
    }

    private boolean isCalorieHigh(float calorie) {
        return calorie > 700;
    }

    private boolean isFatHigh(float fat) {
        return fat > 10;
    }

    private boolean isNatriumHigh(float natrium) {
        return natrium > 700;
    }

    private boolean isProtienHigh(float protien) {
        return protien > 10;
    }

    private boolean isCarbohydrateHigh(float carbohydrate) {
        return carbohydrate > 100;
    }

    //공공데이터 서버에 재료 사용한 메뉴 정보 조회
    //open api 통신 과정
    public GetRecipeOpenResponse getMenuDataFromApi(String ingredient) {
        //서버랑 통신
        RestTemplate restTemplate = new RestTemplate();

        String apiURL = "http://openapi.foodsafetykorea.go.kr/api/"
                + apiKey
                + "/COOKRCP01"
                + "/json"
                + "/1/15"
                +"/RCP_PARTS_DTLS="
                +ingredient;
        System.out.println(apiURL);
        final HttpEntity<String> entity = new HttpEntity<>(null);

        return restTemplate.exchange(apiURL, HttpMethod.GET, entity, GetRecipeOpenResponse.class)
                .getBody(); //여기서 바로 통신한 결과 리턴하는 형식


    }
}


