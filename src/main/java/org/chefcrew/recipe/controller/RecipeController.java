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
import org.chefcrew.jwt.UserId;
import org.chefcrew.recipe.Facade.RecipeFacade;
import org.chefcrew.recipe.dto.request.GetRecipeRequest;
import org.chefcrew.recipe.dto.request.PostUsedRecipeRequest;
import org.chefcrew.recipe.dto.response.GetRecipeResponse;
import org.chefcrew.recipe.dto.response.GetUsedRecipeListResponse;
import org.chefcrew.recipe.service.RecipeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Recipe Controller", description = "레시피 추천 및 사용한 레시피 관련 API")
@SecurityRequirement(name = "jwt-cookie")
@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeFacade recipeFacade;
    private final RecipeService recipeService;

    @Operation(summary = "레시피 추천", description = "주어진 식재료 기반으로 레시피를 추천받습니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "추천 성공"),
            @ApiResponse(responseCode = "400", description = "요청 본문 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "503", description = "공공데이터 서버 에러 시",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<GetRecipeResponse> getRecommendRecipes(
            @RequestBody GetRecipeRequest requestBody
    ) {
        return ResponseEntity.ok().body(recipeService.getRecommendRecipe(requestBody));
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
}
