package com.foodlog.repository;

import com.foodlog.domain.ScheduledMeal;
import com.foodlog.domain.User;
import org.springframework.stereotype.Repository;

import org.springframework.data.jpa.repository.*;
import java.util.List;

/**
 * Spring Data JPA repository for the ScheduledMeal entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ScheduledMealRepository extends JpaRepository<ScheduledMeal,Long> {

    @Query("select scheduled_meal from ScheduledMeal scheduled_meal where scheduled_meal.user.login = ?#{principal.username}")
    List<ScheduledMeal> findByUserIsCurrentUser();

    List<ScheduledMeal> findByNameAndUser(String comment, User currentUser);

    List<ScheduledMeal> findByUser(User currentUser);

    List<ScheduledMeal> findByOrderByTargetTimeDesc();

    List<ScheduledMeal> findByUserOrderByTargetTime(User one);

    List<ScheduledMeal> findByUserOrderByTargetTimeDesc(User currentUser);
}
