package org.chefcrew.recipe.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.chefcrew.recipe.entity.SavedRecipeInfo;
import org.springframework.stereotype.Repository;

@Repository
public class SavedRecipeInfoRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public SavedRecipeInfoRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public void saveRecipe(SavedRecipeInfo savedRecipeInfo) {
        em.persist(savedRecipeInfo);
    }

    public boolean existsByRecipeName(String recipeName) {
        Integer count = query.selectOne()
                .from(savedRecipeInfo)
                .where(savedRecipeInfo.recipeName.eq(recipeName))
                .fetchFirst();
        return count != null && count > 0;
    }

    public SavedRecipeInfo findByRecipeName(String recipeName) {
        return query.selectFrom(savedRecipeInfo)
                .where(savedRecipeInfo.recipeName.eq(recipeName))
                .fetchFirst();
    }

    public SavedRecipeInfo findByRecipeId(long recipeId) {
        return query.selectFrom(savedRecipeInfo)
                .where(savedRecipeInfo.recipeId.eq(recipeId))
                .fetchFirst();
    }
}
