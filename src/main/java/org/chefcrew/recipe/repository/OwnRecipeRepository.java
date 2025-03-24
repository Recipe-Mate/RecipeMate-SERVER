package org.chefcrew.recipe.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.chefcrew.recipe.entity.OwnRecipe;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OwnRecipeRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public OwnRecipeRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public void saveOwnData(OwnRecipe ownRecipe) {
        em.persist(ownRecipe);
    }

    public List<OwnRecipe> findByUserId(long userId) {
        return query.selectFrom(savedRecipeInfo)
                .where(savedRecipeInfo.userId.eq(userId))
                .fetchAll();
    }

}
