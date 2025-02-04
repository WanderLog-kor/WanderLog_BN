package com.tripPlanner.project.domain.makePlanner.repository;

import com.tripPlanner.project.domain.makePlanner.entity.Planner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlannerRepository extends JpaRepository<Planner, Integer> {
//    @Query("SELECT p FROM Planner p JOIN FETCH p.user WHERE p.user.userid = :userId")
//    List<Planner> findByUser_Userid(@Param("userId") String userId);
    // 특정 사용자가 작성한 플래너 목록 조회
    List<Planner> findByUser_Userid(String userId);
//    List<PlannerLike> findByUserId(String userId);
    List<Planner> findByPlannerIDIn(List<Integer> plannerIds);

}