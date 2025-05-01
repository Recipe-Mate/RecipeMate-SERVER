package org.chefcrew.recipe.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Entity
@Slf4j
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SavedRecipeInfo {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    long id;

    String recipeName;

    String recipeImage;

    public SavedRecipeInfo(String recipeName, String recipeImage){
        this.recipeName = recipeName;
        this.recipeImage = recipeImage;
    }
}
