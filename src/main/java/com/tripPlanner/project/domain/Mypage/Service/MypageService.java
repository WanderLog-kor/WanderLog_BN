package com.tripPlanner.project.domain.Mypage.Service;

import com.tripPlanner.project.domain.Mypage.entity.UpdateUserRequest;
import com.tripPlanner.project.domain.like.PlannerLike;
import com.tripPlanner.project.domain.like.PlannerLikeRepository;
import com.tripPlanner.project.domain.makePlanner.dto.DestinationDto;
import com.tripPlanner.project.domain.makePlanner.dto.PlannerDto;
import com.tripPlanner.project.domain.makePlanner.entity.Destination;
import com.tripPlanner.project.domain.makePlanner.repository.DestinationRepository;
import com.tripPlanner.project.domain.makePlanner.repository.PlannerRepository;
import com.tripPlanner.project.domain.signup.entity.UserEntity;
import com.tripPlanner.project.domain.signup.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class MypageService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DestinationRepository destinationRepository;

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

    @Autowired
    private PlannerRepository plannerRepository;

//    public List<PlannerDto> getPlannersByUserId(String userId) {
//        return plannerRepository.findByUser_Userid(userId).stream()
//                .map(planner -> new PlannerDto(planner))
//                .collect(Collectors.toList());
//    }


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


    //Like Service
    @Autowired
    private PlannerLikeRepository likeRepository;


    public List<PlannerLike> getLikedPlanners(String userId) {
        UserEntity user = userRepository.findByUserid(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다."));
        return likeRepository.findByUserId(String.valueOf(user));
    }

}
