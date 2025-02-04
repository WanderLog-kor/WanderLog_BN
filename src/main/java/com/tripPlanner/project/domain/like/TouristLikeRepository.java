package com.tripPlanner.project.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TouristLikeRepository extends JpaRepository<TouristLike, Long> {

    TouristLike findByTouristIdAndUserId(int touristId, String user); // 관광지 ID와 사용자 ID로 조회
    List<TouristLike> findByUserId(String userId); //특정 사용자가 좋아요한 관광지 리스트로 조회
}