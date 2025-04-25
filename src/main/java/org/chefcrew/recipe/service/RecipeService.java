package org.chefcrew.recipe.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.chefcrew.common.exception.CustomException;
import org.chefcrew.recipe.domain.Recipe;
import org.chefcrew.recipe.dto.request.GetRecipeRequest;
import org.chefcrew.recipe.dto.response.GetRecipeOpenResponse;
import org.chefcrew.recipe.dto.response.GetRecipeOpenResponse.RecipeData;
import org.chefcrew.recipe.dto.response.GetRecipeListResponse;
import org.chefcrew.recipe.enums.ValueOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.chefcrew.common.exception.ErrorException.OPEN_API_SERVER_ERROR;

@Service
@Transactional
@RequiredArgsConstructor
public class RecipeService {

    @Value("${apiKey}")
    private String apiKey;

    public String getApiUrl() {
        return "http://openapi.foodsafetykorea.go.kr/api/"
                + apiKey
                + "/COOKRCP01"
                + "/json"
                + "/1/15/";
    }

    public GetRecipeListResponse getRecommendRecipe(GetRecipeRequest getRecipeRequest) {

        Boolean calorieHigh = null;
        Boolean natriumHigh = null;
        Boolean fatHigh = null;
        Boolean protienHigh = null;
        Boolean carbohydrateHigh = null;

        //공공데이터에 메뉴 조회
        GetRecipeOpenResponse getRecipeOpenResponse = getMenuDataFromApiByIngredientName(getRecipeRequest.foodName());
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
        if (getRecipeOpenResponse.cookRcpInfo().row() == null)
            throw new CustomException(OPEN_API_SERVER_ERROR);
        List<RecipeData> recipeResponseList = getRecipeOpenResponse.cookRcpInfo().row()
                .stream()
                .filter(recipeData -> (isAppriateRecipe(finalCalorieHigh, finalFatHigh, finalNatriumHigh,
                        finalProtienHigh,
                        finalCarbohydrateHigh, recipeData)))
                .collect(Collectors.toList());

        List<Recipe> recipeList = recipeResponseList.stream()
                .map(recipeData -> new Recipe(recipeData.recipeName(),
                        recipeData.dish_Img(),
                        Arrays.stream(recipeData.partsDetails().split("\n|, |,"))
                                .map(food -> food.contains(":") ? food.split(": ")[1] : food)
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
    public GetRecipeOpenResponse getMenuDataFromApiByIngredientName(String ingredient) {
        //서버랑 통신
        RestTemplate restTemplate = new RestTemplate();

        System.out.println(getApiUrl() + "RCP_PARTS_DTLS=" + ingredient);
        final HttpEntity<String> entity = new HttpEntity<>(null);

        return restTemplate.exchange(getApiUrl() + "RCP_PARTS_DTLS=" + ingredient,
                        HttpMethod.GET,
                        entity,
                        GetRecipeOpenResponse.class)
                .getBody(); //여기서 바로 통신한 결과 리턴하는 형식


    }

    //공공데이터 서버에 레시피명으로 메뉴 정보 조회
    //open api 통신 과정
    public GetRecipeOpenResponse getMenuDataFromApiByRecipeName(String recipeName) {
        //서버랑 통신
        RestTemplate restTemplate = new RestTemplate();

        System.out.println(getApiUrl() + "RCP_NM=" + recipeName);
        final HttpEntity<String> entity = new HttpEntity<>(null);

        GetRecipeOpenResponse response = restTemplate.exchange(getApiUrl() + "RCP_NM=" + recipeName,
                        HttpMethod.GET,
                        entity,
                        GetRecipeOpenResponse.class)
                .getBody();

        if (response.cookRcpInfo().total_count() == 0) {
            return null;
        }
        return response;
    }

    ; //여기서 바로 통신한 결과 리턴하는 형식



/*
   //공공데이터 서버에 아이디로 조회
    //open api 통신 과정
    public GetRecipeOpenResponse getMenuDataFromApiById(String ingredient) {
        //서버랑 통신
        RestTemplate restTemplate = new RestTemplate();

        System.out.println(getApiUrl() + ingredient);
        final HttpEntity<String> entity = new HttpEntity<>(null);

        return restTemplate.exchange(getApiUrl() + ingredient, HttpMethod.GET, entity, GetRecipeOpenResponse.class)
                .getBody(); //여기서 바로 통신한 결과 리턴하는 형식


    }*/
}


