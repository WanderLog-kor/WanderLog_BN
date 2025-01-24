package com.tripPlanner.project.domain.board;

import com.tripPlanner.project.domain.makePlanner.entity.Planner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Planner, Integer> {
    // 공개된 플래너만 페이징 처리
    Page<Planner> findByIsPublicTrue(Pageable pageable);

    Page<Planner> findByAreaAndIsPublicTrue(String area, Pageable pageable);

    // 특정 사용자(userId)와 일치하는 플래너를 조회
    Page<Planner> findByUser_Userid(String userId, Pageable pageable);

    // 특정 지역(area)에 해당하는 플래너를 조회
    Page<Planner> findByArea(String area, Pageable pageable);

    // 모든 플래너 조회
    Page<Planner> findAll(Pageable pageable);
}
