package com.tripPlanner.project.domain.like;

import com.tripPlanner.project.domain.signup.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TouristLikeRepository extends JpaRepository<TouristLike, Long> {

    TouristLike findByTouristIdAndUserId(int touristId, String user); // 관광지 ID와 사용자 ID로 조회

}