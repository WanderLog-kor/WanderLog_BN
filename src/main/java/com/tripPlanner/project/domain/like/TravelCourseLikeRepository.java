package com.tripPlanner.project.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TravelCourseLikeRepository extends JpaRepository<TravelCourseLike, Long> {
    TravelCourseLike findByTravelCourseIdAndUserId(int travelCourseId, String user); // 여행코스 ID와 사용자 ID로 조회
    List<TravelCourseLike> findByUserId(String userId); //특정 사용자가 좋아요한 여행코스 리스트로 조회

}
