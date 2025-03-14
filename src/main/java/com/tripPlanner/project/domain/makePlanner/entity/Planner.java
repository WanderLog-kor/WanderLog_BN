package com.tripPlanner.project.domain.makePlanner.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tripPlanner.project.domain.board.BoardDto;
import com.tripPlanner.project.domain.like.PlannerLike;
import com.tripPlanner.project.domain.makePlanner.dto.DestinationDto;
import com.tripPlanner.project.domain.makePlanner.dto.PlannerDto;
import com.tripPlanner.project.domain.signup.entity.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Table(name = "Planner")
@Entity
@AllArgsConstructor
@Builder
@Getter
public class Planner {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int plannerID; // 여행 일정 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference  // UserEntity와의 순환 참조 방지
    private UserEntity user;

    @Column(name = "plannerTitle", nullable = false)
    private String plannerTitle;

    @Column(name = "createAt", nullable = false, updatable = false)
    private LocalDateTime createAt = LocalDateTime.now(); // 등록일자

    @Column(name = "updateAt", nullable = false)
    private LocalDateTime updateAt = LocalDateTime.now(); // 수정일자

    @Column(name = "description")
    private String description;

    @Column(name = "day", nullable = false)
    private int day; // 여행 기간

    @Column(name = "isPublic", nullable = false)
    private boolean isPublic; // 공개 여부 (생성 시 선택)

    @Column(name = "area", nullable = false)
    private String area;

    @Column(name = "startDate")
    private LocalDate startDate;

    @Column(name = "endDate")
    private LocalDate endDate;

    @OneToMany(mappedBy = "plannerId", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference // Like와의 순환 참조 방지
    private List<PlannerLike> plannerLikes = new ArrayList<>();

    public PlannerDto toDto(Planner planner, List<DestinationDto> destinations) {
        return PlannerDto.builder()
                .plannerID(planner.getPlannerID())
                .user(planner.getUser())
                .plannerTitle(planner.getPlannerTitle())
                .createAt(planner.getCreateAt())
                .updateAt(planner.getUpdateAt())
                .startDate(planner.getStartDate())
                .endDate(planner.getEndDate())
                .day(planner.getDay())
                .area(planner.getArea())
                .isPublic(planner.isPublic())
                .description(planner.getDescription())
                .destinations(destinations)
                .build();
    }

}