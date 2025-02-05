package com.tripPlanner.project.domain.Mypage.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tripPlanner.project.domain.Mypage.entity.UpdateUserRequest;
import com.tripPlanner.project.domain.like.*;
import com.tripPlanner.project.domain.makePlanner.dto.DestinationDto;
import com.tripPlanner.project.domain.makePlanner.dto.PlannerDto;
import com.tripPlanner.project.domain.makePlanner.entity.Destination;
import com.tripPlanner.project.domain.makePlanner.entity.Planner;
import com.tripPlanner.project.domain.makePlanner.repository.DestinationRepository;
import com.tripPlanner.project.domain.makePlanner.repository.PlannerRepository;
import com.tripPlanner.project.domain.signup.repository.UserRepository;
import com.tripPlanner.project.domain.tourist.ApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MypageService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DestinationRepository destinationRepository;
    //Like Service
    @Autowired
    private PlannerLikeRepository likeRepository;
    @Autowired
    private PlannerRepository plannerRepository;
    @Autowired
    private TouristLikeRepository touristLikeRepository;
    @Autowired
    private TravelCourseLikeRepository travelCourseLikeRepository;

    private final WebClient webClient;
    @Autowired
    private ApiService apiService;

    public MypageService(WebClient.Builder webClientBuilder) {
        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        // 쿼리 파라미터 인코딩을 하지 않도록 설정
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        // WebClient를 생성할 때 이 factory를 사용
        this.webClient = WebClient.builder()
                .uriBuilderFactory(factory)
                .build();
    }

    public void validatePassword(String password) {
        String passwordRegex = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d@$!%*?&]{8,15}$";
        if (password == null || password.isEmpty()) {
            throw new IllegalArgumentException("비밀번호를 입력해주세요.");
        }
        if (password.contains(" ")) {
            throw new IllegalArgumentException("비밀번호에 공백은 사용할 수 없습니다.");
        }
        if (!password.matches(passwordRegex)) {
            throw new IllegalArgumentException("비밀번호는 영문+숫자 조합, 8~15자리여야 합니다.");
        }
    }

    public void validateEmail(String email) {
        String emailRegex = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("이메일을 입력해주세요.");
        }
        if (!email.matches(emailRegex)) {
            throw new IllegalArgumentException("유효한 이메일 형식을 입력하세요.");
        }
//        // 이메일 중복 검사
//        log.info("Validating email: {}", email); // 디버깅 추가
//        if (userRepository.existsByEmail(email)) {
//            log.warn("이미 사용 중인 이메일입니다: {}", email); // 디버깅 추가
//            throw new IllegalArgumentException("이미 사용 중인 이메일입니다.");
//        }
    }

    public void validateUsername(String username) {
        String usernameRegex = "^[a-zA-Z가-힣]+$";
        if (username == null || username.isEmpty()) {
            throw new IllegalArgumentException("사용자 이름을 입력해주세요.");
        }
        if (!username.matches(usernameRegex)) {
            throw new IllegalArgumentException("이름은 영어 및 한글만 사용할 수 있습니다.");
        }
    }

    public void validateUpdateRequest(UpdateUserRequest request) {
        if (request.getUsername() != null) {
            validateUsername(request.getUsername());
        }
        if (request.getEmail() != null) {
            validateEmail(request.getEmail());
        }
        if (request.getPassword() != null) {
            validatePassword(request.getPassword());
        }
    }


//    public List<PlannerDto> getPlannersByUserId(String userId) {
//        return plannerRepository.findByUser_Userid(userId).stream()
//                .map(planner -> new PlannerDto(planner))
//                .collect(Collectors.toList());
//    }

    //유저 아이디에 맞는 플래너를 받아오는 서비스 함수
    public List<PlannerDto> getPlannersByUserId(String userId) {

        return plannerRepository.findByUser_Userid(userId).stream()

                .map(planner -> {
                    List<DestinationDto> destinations = destinationRepository.findByPlanner_PlannerID(planner.getPlannerID())
                            .stream()
                            .map(Destination::toDto)
                            .collect(Collectors.toList());

                    return PlannerDto.builder()
                            .plannerID(planner.getPlannerID())
                            .plannerTitle(planner.getPlannerTitle())
//                            .user(planner.getUser())
                            .userId(userId)
                            .area(planner.getArea())
                            .day(planner.getDay())
                            .description(planner.getDescription())
                            .isPublic(planner.isPublic())
                            .createAt(planner.getCreateAt())
                            .updateAt(planner.getUpdateAt())
                            .startDate(planner.getStartDate())
                            .endDate(planner.getEndDate())
                            .destinations(destinations) // 🔥 destination을 DTO로 변환해서 포함
                            .build();
                })
                .collect(Collectors.toList());
    }

    //유저 아이디에 맞는 좋아요한 플래너를 받아오는 서비스 함수
    public List<PlannerDto> getLikedPlanners(String userId) {

        List<PlannerLike> likePlanners = likeRepository.findByUserId(userId);
        
        //좋아요한 플래너 ID 목록 추출
        List<Integer> plannerIds = likePlanners.stream()
                .map(PlannerLike::getPlannerId)
                .collect(Collectors.toList());
        
        List<Planner> plannerList = plannerRepository.findByPlannerIDIn(plannerIds);

        log.info("플래너 리스트 {}",plannerList);
        log.info("좋아요 리스트 {}",likePlanners);
        return plannerList.stream()
                .map(planner -> {
                    List<DestinationDto> destinations = destinationRepository.findByPlanner_PlannerID(planner.getPlannerID())
                            .stream()
                            .map(Destination::toDto)
                            .collect(Collectors.toList());

                    return PlannerDto.builder()
                            .plannerID(planner.getPlannerID())
                            .plannerTitle(planner.getPlannerTitle())
                            .area(planner.getArea())
                            .day(planner.getDay())
                            .description(planner.getDescription())
                            .isPublic(planner.isPublic())
                            .createAt(planner.getCreateAt())
                            .updateAt(planner.getUpdateAt())
                            .startDate(planner.getStartDate())
                            .endDate(planner.getEndDate())
                            .destinations(destinations) // 🔥 Destination 정보 추가
                            .build();
                })
                .collect((Collectors.toList()));
    }
    
    //좋아요한 관광지를 받아오는 함수
    public List<JsonNode> getLikedTourists(String userid){

        List<TouristLike> likedTourists = touristLikeRepository.findByUserId(userid);
        log.info("좋아요한 관광지 : {}",likedTourists);

        List<JsonNode> touristDataList = new ArrayList<>();

        for(TouristLike touristLike : likedTourists){
            String contentId = String.valueOf(touristLike.getTouristId());

            try{
                String response = String.valueOf(apiService.getDetailCommon(contentId).block());

                if(response != null){
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(response);
                    touristDataList.add(jsonNode);
                }
            } catch (Exception e){
                log.error("관광지 데이터 조회 중 오류 발생 (contentId={}): {}", contentId, e.getMessage());
            }
        }
        return touristDataList;
    }

    //좋아요 누른 여행코스 받아오는 함수
    public List<JsonNode> getLikedTravelCourses(String userid){

        List<TravelCourseLike> likedTravelCourses = travelCourseLikeRepository.findByUserId(userid);
        log.info("좋아요한 여행코스 : {}",likedTravelCourses);

        List<JsonNode> travelCourseList = new ArrayList<>();

        for(TravelCourseLike travelCourseLike : likedTravelCourses){
            String contentId = String.valueOf(travelCourseLike.getTravelCourseId());

            try{
                String response = String.valueOf(apiService.getDetailCommon(contentId).block());

                if(response != null){
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode jsonNode = objectMapper.readTree(response);
                    travelCourseList.add(jsonNode);
                }
            } catch (Exception e){
                log.error("여행코스 데이터 조회 중 오류 발생 (contentId={}): {}", contentId, e.getMessage());
            }
        }
        return travelCourseList;
    }

}
