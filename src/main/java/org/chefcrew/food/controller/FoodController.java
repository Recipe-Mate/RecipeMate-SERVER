package org.chefcrew.food.controller;

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
import org.chefcrew.food.dto.request.AddFoodRequest;
import org.chefcrew.food.dto.request.DeleteFoodRequest;
import org.chefcrew.food.dto.request.PostAmountUpdateRequest;
import org.chefcrew.food.dto.response.GetOwnFoodResponse;
import org.chefcrew.food.service.FoodService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Food Controller", description = "식재료 관련 API (추가, 조회, 업데이트, 삭제)")
@SecurityRequirement(name = "jwt-cookie")
@RestController
@RequestMapping("/food")
@RequiredArgsConstructor
public class FoodController {
    private final FoodService foodService;

    @Operation(summary = "식재료 추가", description = "새로 구매한 식재료 리스트를 추가합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "식재료 추가 성공"),
            @ApiResponse(responseCode = "400", description = "유저가 존재하지 않습니다",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)    public ResponseEntity<Void> saveNewFoodList(
            @Parameter(hidden = true) @UserId Long userId,
            @RequestPart("foodDataList") AddFoodRequest requestBody,
            @RequestPart(value = "images", required = false) List<MultipartFile> imageList
    ) {
        foodService.saveFoodList(userId, requestBody, imageList);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "유저의 소유 식재료 리스트 조회", description = "유저가 보유한 식재료 리스트를 조회합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "조회 성공"),
            @ApiResponse(responseCode = "401", description = "존재하지 않는 유저일 때",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/ownlist")
    private ResponseEntity<GetOwnFoodResponse> getOwnFoodList(
            @Parameter(hidden = true) @UserId Long userId
    ) {
        return ResponseEntity.ok()
                .body(new GetOwnFoodResponse(foodService.getOwnedFoodList(userId)));
    }

    //먹은 양 체크하는 api
    @Operation(summary = "식재료 소유 양 업데이트", description = "유저가 소유한 식재료의 양을 업데이트합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "업데이트 성공"),
            @ApiResponse(responseCode = "400", description = "요청 본문 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/amount-update")
    private ResponseEntity<Void> updateFoodAmount(
            @Parameter(hidden = true) @UserId Long userId,
            @RequestBody PostAmountUpdateRequest requestBody
    ){
        foodService.updateFoodAmountAndUnit(userId, requestBody);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "식재료 삭제", description = "다 먹은 식재료를 냉장고에서 제거합니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "삭제 성공"),
            @ApiResponse(responseCode = "400", description = "삭제 요청 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping
    private ResponseEntity<Void> deleteUsedFood(
            @Parameter(hidden = true) @UserId Long userId,
            @RequestBody DeleteFoodRequest requestBody) {
        foodService.deleteFood(userId, requestBody);
        return ResponseEntity.ok().build();
    }
}
