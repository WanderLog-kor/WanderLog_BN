package com.tripPlanner.project.domain.board;

import com.tripPlanner.project.domain.like.PlannerLikeRepository;
import com.tripPlanner.project.domain.makePlanner.entity.Destination;
import com.tripPlanner.project.domain.makePlanner.entity.Planner;
import com.tripPlanner.project.domain.makePlanner.repository.DestinationRepository;
import com.tripPlanner.project.domain.makePlanner.repository.PlannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final DestinationRepository destinationRepository;
    private final PlannerRepository plannerRepository;
    private final PlannerLikeRepository plannerLikeRepository;

    // 플래너 전체를 가져와서 게시판에 띄우기
    public Page<BoardDto> getPlanners(String userId, String area, Boolean myCourses, Pageable pageable) {
        if (myCourses != null && myCourses && userId != null) {
            // 로그 추가
            System.out.println("내가 작성한 코스만 보기 - userId: " + userId);
            return boardRepository.findByUser_Userid(userId, pageable)
                    .map(planner -> toBoardDtoWithThumbnail(planner));
        } else if (area != null) {
            // 지역별 필터링
            System.out.println("지역별 필터링 - area: " + area);
            return boardRepository.findByAreaAndIsPublicTrue(area, pageable)
                    .map(planner -> toBoardDtoWithThumbnail(planner));
        } else {
            // 모든 공개된 플래너 조회
            System.out.println("모든 공개된 플래너 조회");
            return boardRepository.findByIsPublicTrue(pageable)
                    .map(planner -> toBoardDtoWithThumbnail(planner));
        }
    }



    // 공개된 플래너를 가져와서 반환 (메인페이지)
    public Page<BoardDto> getPlannersForBoard(Pageable pageable) {
        // 공개된 플래너를 페이징 처리하여 조회
        return boardRepository.findByIsPublicTrue(pageable)
                .map(planner -> toBoardDtoWithThumbnail(planner));  // 썸네일 추가
    }

    // BoardDto 변환 메서드
    private BoardDto toBoardDtoWithThumbnail(Planner planner) {
        // 썸네일 이미지 가져오기
        String thumbnailImage = getThumbnailImage(planner.getPlannerID());

        // 좋아요 개수 가져오기
        int likeCount = plannerLikeRepository.countByPlannerId(planner.getPlannerID());

        return BoardDto.builder()
                .plannerID(planner.getPlannerID())
                .plannerTitle(planner.getPlannerTitle())
                .createAt(planner.getCreateAt())
                .startDate(planner.getStartDate())
                .endDate(planner.getEndDate())
                .day(planner.getDay())
                .username(planner.getUser().getUsername())
                .thumbnailImage(thumbnailImage)  // 썸네일 이미지 설정
                .area(planner.getArea())
                .description(planner.getDescription())
                .userId(planner.getUser().getUserid())
                .userProfileImg(planner.getUser().getImg())
                .isPublic(planner.isPublic())
                .likeCount(likeCount)
                .build();
    }


    // 플래너의 썸네일 이미지를 가져오는 메서드
    public String getThumbnailImage(int plannerID) {
        // 플래너 조회
        Planner planner = boardRepository.findById(plannerID)
                .orElseThrow(() -> new IllegalArgumentException("플래너를 찾을 수 없습니다."));

        // 첫 번째 날과 첫 번째 dayOrder에 해당하는 Destination 이미지 조회
        Destination destination = destinationRepository.findFirstDestinationForPlanner(plannerID, 1, 1);

        // 해당 Destination의 이미지 URL 반환
        return destination != null ? destination.getImage() : null;
    }

    // 여행 계획의 전체 개수를 반환하는 메서드
    public int getTotalPlans() {
        return (int) plannerRepository.count();
    }
}
