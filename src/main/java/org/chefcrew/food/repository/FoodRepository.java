package org.chefcrew.food.repository;

import static org.chefcrew.food.entity.QFood.food;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPADeleteClause;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.List;
import org.chefcrew.food.domain.FoodAmount;
import org.chefcrew.food.entity.Food;
import org.chefcrew.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class FoodRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public FoodRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public void saveFood(Food foodList) {
        em.persist(foodList);
    }

    //특정 유저가 소유한 식재료 조회
    public List<Food> findByUserId(long userId) {
        return query.selectFrom(food)
                .where(food.userId.eq(userId))
                .fetch();
    }

    public boolean existsByUserId(long userId){
        Integer count = query.selectOne()
                .from(food)
                .where(food.userId.eq(userId))
                .fetchFirst();
        return count != null && count > 0;
    }

    public void deleteFood(long userId, List<String> foodNameList) {
        new JPADeleteClause(em, food)
                .where(food.foodName.in(foodNameList)
                        .and(food.userId.eq(userId)))
                .execute();
    }

    public boolean existsByFoodNameAndUserId(String foodName, long userId) {
        Integer count = query.selectOne()
                .from(food)
                .where(food.foodName.eq(foodName)
                        .and(food.userId.eq(userId)))
                .fetchFirst();
        return count != null && count > 0;

    }

    public List<Food> getAllByUser(User user){
        return query.selectFrom(food)
                .where(food.userId.eq(user.getUserId()))
                .fetch();
    }

    public void deleteAllById(List<Long> foodIdList){
        new JPADeleteClause(em, food)
                .where(food.foodId.in(foodIdList))
                .execute();
        em.flush();
    }

    public List<Food> getByUserIdAndFoodId(long userId, List<FoodAmount> foodAmountList) {
        BooleanBuilder builder = new BooleanBuilder();
        for (FoodAmount foodAmount : foodAmountList) {
            builder.or(
                    food.foodId.eq(foodAmount.getFoodId())
                            .and(food.userId.eq(userId))
            );
        }

        List<Food> result = query
                .selectFrom(food)
                .where(builder)
                .fetch();
        return result;
    }
}
