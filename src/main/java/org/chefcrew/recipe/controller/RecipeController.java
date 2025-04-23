package org.chefcrew.recipe.controller;

import lombok.RequiredArgsConstructor;
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

@RestController
@RequestMapping("/recipe")
@RequiredArgsConstructor
public class RecipeController {
    private final RecipeFacade recipeFacade;
    private final RecipeService recipeService;

    @PostMapping
    public ResponseEntity<GetRecipeResponse> getRecommendRecipes(
            @RequestBody GetRecipeRequest requestBody
    ) {
        return ResponseEntity.ok().body(recipeService.getRecommendRecipe(requestBody));
    }

    @PostMapping("/used")
    public ResponseEntity<Void> postAsUsedRecipe(
            @UserId Long userId,
            @RequestBody PostUsedRecipeRequest requestBody) {
        recipeFacade.postAsUsedRecipe(userId, requestBody);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/used")
    public ResponseEntity<GetUsedRecipeListResponse> getUsedRecipeList(
            @UserId Long userId
    ) {
        return ResponseEntity.ok(recipeFacade.getUsedRecipeList(userId));
    }
}
