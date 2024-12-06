package org.chefcrew.food.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.chefcrew.food.entity.Food;
import org.springframework.stereotype.Repository;

@Repository
public class FoodRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public FoodRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }
    public void saveFoodList(List<Food> foodList){
        em.persist(foodList);
    }
}
