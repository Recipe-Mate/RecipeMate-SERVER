package org.chefcrew.user.repository;

import static org.chefcrew.user.entity.QUser.user;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.chefcrew.user.entity.User;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {
    private final EntityManager em;
    private final JPAQueryFactory query;

    public UserRepository(EntityManager em) {
        this.em = em;
        this.query = new JPAQueryFactory(em);
    }

    public void save(User user) {
        em.persist(user);
    }

    public User findById(long userId) {
        return query.selectFrom(user)
                .where(user.userId.eq(userId))
                .fetchFirst();
    }

/*    public boolean existByEmail(String email) {
        Long count = query.select(user.count())
                .from(user)
                .where(user.email.eq(email))
                .fetchOne();
        return count != null && count > 0;
    }

    public User findByEmail(String email) {
        return query.selectFrom(user)
                .where(user.email.eq(email))
                .fetchFirst();
    }*/

    public Optional<User> findByRefreshToken(String refreshToken) {
        return Optional.ofNullable(query.selectFrom(user)
                .where(user.refreshToken.eq(refreshToken))
                .fetchFirst());
    }

    public Optional<User> findBySocialId(String socialId) {
        return Optional.ofNullable(query.selectFrom(user)
                .where(user.socialId.eq(socialId))
                .fetchFirst());
    }

    public boolean existsBySocialId(String socialId) {
        Integer count = query.selectOne()
                .from(user)
                .where(user.socialId.eq(socialId))
                .fetchFirst();
        return count != null && count > 0;
    }

    public Optional<User> findByUserId(Long userId) {
        return Optional.ofNullable(query.selectFrom(user)
                .where(user.userId.eq(userId))
                .fetchFirst());
    }

    public Long deleteByUserId(Long userId) {
        return query.delete(user)
                .where(user.userId.eq(userId))
                .execute();
    }
}
