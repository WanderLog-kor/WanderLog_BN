package com.tripPlanner.project.domain.like;


import com.tripPlanner.project.domain.makePlanner.entity.Planner;
import com.tripPlanner.project.domain.signup.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlannerLikeRepository extends JpaRepository<PlannerLike, Long> {

    boolean existsByPlannerIdAndUserId(int plannerID, String userId);

    void deleteByPlannerIdAndUserId(int plannerID, String userId);

    int countByPlannerId(int plannerID);

    List<PlannerLike> findByUserId(String userId);
}
