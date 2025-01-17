package com.tripPlanner.project.domain.like;

import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelCourseLikeRepository extends JpaRepository<TravelCourseLike, Long> {
    TravelCourseLike findByTravelCourseIdAndUserId(int travelCourseId, String user); // 여행코스 ID와 사용자 ID로 조회


}
