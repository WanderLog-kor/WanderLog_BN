package com.tripPlanner.project.domain.like;


import com.tripPlanner.project.domain.makePlanner.entity.Planner;

import com.tripPlanner.project.domain.signup.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class LikeService {
    private final PlannerLikeRepository likeRepository;
    private final TouristLikeRepository touristLikeRepository;
    private final TravelCourseLikeRepository travelCourseLikeRepository;

    // 좋아요 상태 및 카운트 조회
    public LikeStatusResponse getPlannerLikeStatus(int plannerID, String userId) {
        boolean isLiked = likeRepository.existsByPlannerIdAndUserId(plannerID, userId);
        int likeCount = likeRepository.countByPlannerId(plannerID);
        return new LikeStatusResponse(isLiked, likeCount);
    }

    // 좋아요 토글 처리
    public LikeStatusResponse togglePlannerLike(int plannerID, String userId) {
        boolean isLiked = likeRepository.existsByPlannerIdAndUserId(plannerID, userId);
        if (isLiked) {
            // 좋아요 제거
            likeRepository.deleteByPlannerIdAndUserId(plannerID, userId);
        } else {
            // 좋아요 추가
            PlannerLike like = new PlannerLike();
            like.setPlannerId(plannerID);
            like.setUserId(userId);
            likeRepository.save(like);
        }

        int likeCount = likeRepository.countByPlannerId(plannerID);
        return new LikeStatusResponse(!isLiked, likeCount);
    }

    // 관광지에 좋아요 상태 토글
    public void toggleTouristLike(int touristId, String userId) {
        // 이미 좋아요를 눌렀는지 확인
        TouristLike existingLike = touristLikeRepository.findByTouristIdAndUserId(touristId, userId);
        if (existingLike != null) {
            // 이미 눌렀으면 삭제 (좋아요 취소)
            touristLikeRepository.delete(existingLike);
        } else {
            // 좋아요 상태 없으면 추가
            TouristLike touristLike = new TouristLike();
            touristLike.setTouristId(touristId);
            touristLike.setUserId(userId);
            touristLikeRepository.save(touristLike);
        }
    }

    // 관광지에 좋아요 상태 확인
    public boolean isTouristLiked(int touristId, String userId) {
        // 관광지와 사용자가 존재하는지 확인
        TouristLike touristLike = touristLikeRepository.findByTouristIdAndUserId(touristId, userId);
        return touristLike != null; // 관광지를 좋아요 했다면 true 반환, 아니면 false 반환
    }

    // 여행지 코스에 좋아요 상태 토글
    public void toggleTravelCourseLike(int travelCourseId, String userId) {
        // 이미 좋아요를 눌렀는지 확인
        TravelCourseLike existingLike = travelCourseLikeRepository.findByTravelCourseIdAndUserId(travelCourseId, userId);
        if (existingLike != null) {
            // 이미 눌렀으면 삭제 (좋아요 취소)
            travelCourseLikeRepository.delete(existingLike);
        } else {
            // 좋아요 상태 없으면 추가
            TravelCourseLike travelCourseLike = new TravelCourseLike();
            travelCourseLike.setTravelCourseId(travelCourseId);
            travelCourseLike.setUserId(userId);
            travelCourseLikeRepository.save(travelCourseLike);
        }
    }

    // 여행지 코스에 좋아요 상태 확인
    public boolean isTravelCourseLiked(int travelCourseId, String userId) {
        // 관광지와 사용자가 존재하는지 확인
        TravelCourseLike touristLike = travelCourseLikeRepository.findByTravelCourseIdAndUserId(travelCourseId, userId);
        return touristLike != null; // 관광지를 좋아요 했다면 true 반환, 아니면 false 반환
    }
}
