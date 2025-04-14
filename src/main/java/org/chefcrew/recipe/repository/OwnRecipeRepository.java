package org.chefcrew.recipe.repository;

import static org.chefcrew.recipe.entity.QOwnRecipe.ownRecipe;

import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.chefcrew.recipe.entity.OwnRecipe;
import org.springframework.stereotype.Repository;

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

    public List<OwnRecipe> findAllByUserId(long userId) {
        return query.selectFrom(ownRecipe)
                .where(ownRecipe.userId.eq(userId))
                .fetchAll()
                .stream().toList();
    }

    public void deleteAllById(List<Long> idList) {
        new JPADeleteClause(em, ownRecipe)
                .where(ownRecipe.id.in(idList))
                .execute();
        em.flush();
    }

}
