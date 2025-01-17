package com.tripPlanner.project.domain.like;

import com.tripPlanner.project.domain.login.auth.PrincipalDetail;
import com.tripPlanner.project.domain.makePlanner.dto.DestinationDto;
import com.tripPlanner.project.domain.makePlanner.entity.Destination;
import com.tripPlanner.project.domain.makePlanner.repository.DestinationRepository;
import com.tripPlanner.project.domain.makePlanner.service.DestinationService;
import com.tripPlanner.project.domain.tourist.ApiRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final DestinationRepository destinationRepository;
    private final DestinationService destinationService;
    private final LikeService likeService;

    // 플래너 ID에 해당하는 destination 리스트 반환
    @GetMapping("/planner/board/destination")
    public List<DestinationDto> getDestinationsByPlannerId(@RequestParam("plannerID") int plannerID) {
        List<Destination> destinations = destinationRepository.findByPlanner_PlannerID(plannerID);

        // Destination 엔티티를 DTO로 변환하여 반환
        return destinations.stream()
                .map(Destination::toDto) // Destination을 DestinationDto로 변환
                .collect(Collectors.toList());
    }

    // 좋아요 상태 및 카운트 조회
    @GetMapping("/planner/board/likeStatus")
    public ResponseEntity<LikeStatusResponse> getLikeStatus(
            @RequestParam(name = "plannerID") int plannerID,
            @RequestParam(name = "userId") String userId
    ) {

        LikeStatusResponse response = likeService.getPlannerLikeStatus(plannerID, userId);
        return ResponseEntity.ok(response);
    }

    // 좋아요 토글
    @PostMapping("/planner/board/toggleLike")
    public ResponseEntity<LikeStatusResponse> toggleLike(
            @RequestBody PlannerLikeRequest likeRequest
    ) {

        LikeStatusResponse response = likeService.togglePlannerLike(likeRequest.getPlannerID(), likeRequest.getUserId());
        return ResponseEntity.ok(response);
    }


    // destination 리스트 클릭 시 tourist페이지로 가서 정보를 띄워주게 할 contentId를 얻어오기
    @PostMapping("/destination-to-tourist")
    public Mono<String> destinaionToTourist(@RequestBody ApiRequest apiRequest) {
        return destinationService.getLocationBasedList(apiRequest.getMapX(), apiRequest.getMapY());
    }

    // 관광지 좋아요 눌렀는지 확인 여부 (로그인 상태에서)
    @GetMapping("/tourist/likeStatus")
    public boolean checkTouristLikeStatus(@RequestParam(name = "touristId") int touristId, @RequestParam(name = "userId") String userId) {
        return likeService.isTouristLiked(touristId, userId);
    }

    // 관광지 좋아요 기능 (추가와 삭제)
    @PostMapping("/tourist/toggleLike")
    public String toggleTouristLike(@RequestBody TouristLikeRequest touristLikeRequest) {

        if (touristLikeRequest.getUserId() == null) {
            return "로그인이 필요합니다";
        }
        likeService.toggleTouristLike(touristLikeRequest.getTouristId(), touristLikeRequest.getUserId());
        return "success";
    }

    // 여행지 코스 좋아요 눌렀는지 확인 여부 (로그인 상태에서)
    @GetMapping("/travelCourse/likeStatus")
    public boolean checktravelCourseLikeStatus(@RequestParam(name = "travelCourseId") int travelCourseId, @RequestParam(name = "userId") String userId) {
        return likeService.isTravelCourseLiked(travelCourseId, userId);
    }

    // 여행지 코스 좋아요 기능 (추가와 삭제)
    @PostMapping("/travelCourse/toggleLike")
    public String toggletravelCourseLike(@RequestBody TravelCourseLikeRequest travelCourseLikeRequest) {

        if (travelCourseLikeRequest.getUserId() == null) {
            return "로그인이 필요합니다";
        }
        likeService.toggleTravelCourseLike(travelCourseLikeRequest.getTravelCourseId(), travelCourseLikeRequest.getUserId());
        return "success";
    }
}
