package org.chefcrew.recipe.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.chefcrew.common.response.ErrorResponse;
import org.chefcrew.external.gemini.Service.GeminiService;
import org.chefcrew.jwt.UserId;
import org.chefcrew.recipe.Facade.RecipeFacade;
import org.chefcrew.recipe.dto.request.GetRecipeRequest;
import org.chefcrew.recipe.dto.request.PostUsedRecipeRequest;
import org.chefcrew.recipe.dto.response.GetRecipeDataResponse;
import org.chefcrew.recipe.dto.response.GetRecipeListResponse;
import org.chefcrew.recipe.dto.response.GetReplicableFoodResponse;
import org.chefcrew.recipe.dto.response.GetUsedRecipeListResponse;
import org.chefcrew.recipe.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Tag(name = "Recipe Controller", description = "레시피 추천 및 사용한 레시피 관련 API")
@SecurityRequirement(name = "jwt-cookie")
@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeFacade recipeFacade;
    private final RecipeService recipeService;
    private final GeminiService geminiService;

    @Operation(summary = "레시피 추천", description = "주어진 식재료 기반으로 레시피를 추천받습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추천 성공"),
            @ApiResponse(responseCode = "400", description = "요청 본문 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "공공데이터 서버 에러 시",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<GetRecipeListResponse> getRecommendRecipes(
            @RequestBody GetRecipeRequest requestBody
    ) {
        return ResponseEntity.ok().body(recipeService.getRecommendRecipe(requestBody));
    }

    @Operation(summary = "레시피 검색", description = "레시피명으로 레시피 정보 조회 api")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추천 성공"),
            @ApiResponse(responseCode = "400", description = "요청 본문 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "공공데이터 서버 에러 시",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/recipe-name")
    public ResponseEntity<GetRecipeDataResponse> getRecipeData(
            @RequestParam String name
    ) {
        return ResponseEntity.ok().body(recipeFacade.getRecipeDataByRecipeName(name));
    }

    @Operation(summary = "레시피 저장", description = "선택한 레시피를 사용한 레시피로 저장합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "등록 성공"),
            @ApiResponse(responseCode = "400", description = "저장하려는 레시피 정보가 올바르지 않은 경우",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/used")
    public ResponseEntity<Void> postAsUsedRecipe(
            @Parameter(hidden = true) @UserId Long userId,
            @RequestBody PostUsedRecipeRequest requestBody) {
        recipeFacade.postAsUsedRecipe(userId, requestBody);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "저장한 레시피 목록 조회", description = "유저가 사용한 레시피 리스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/used")
    public ResponseEntity<GetUsedRecipeListResponse> getUsedRecipeList(
            @Parameter(hidden = true) @UserId Long userId
    ) {
        return ResponseEntity.ok(recipeFacade.getUsedRecipeList(userId));
    }

    @Operation(summary = "대체 가능한 식재료 조회", description = "특정 레시피의 특정 식재료를 대체할 수 있는 음식을 검색합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/alternative-food")
    public Mono<ResponseEntity<GetReplicableFoodResponse>> getAlternativeFood(
            @RequestParam String menu,
            @RequestParam String replaceFood) {
        return geminiService.getAlternativeFoodAsync(menu, replaceFood)
                .map(GetReplicableFoodResponse::new)
                .map(ResponseEntity::ok);
    }
}
