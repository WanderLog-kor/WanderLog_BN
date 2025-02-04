package com.tripPlanner.project.domain.makePlanner.dto;

import com.tripPlanner.project.domain.makePlanner.entity.Planner;
import com.tripPlanner.project.domain.signup.entity.UserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlannerDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int plannerID;
    @Column

    private UserEntity user;
    @Column
    private String plannerTitle;
    @Column
    private LocalDateTime createAt;
    @Column
    private LocalDateTime updateAt;
    @Column
    private int day;
    @Column
    private boolean isPublic;
    @Column
    private String description;
    @Column
    private String area;
    @Column
    private LocalDate startDate;
    @Column
    private LocalDate endDate;
    private String userid;

    private List<DestinationDto> destinations;


    public static Planner dtoToEntity(PlannerDto plannerDto) {
        return Planner.builder()
                .plannerID(plannerDto.getPlannerID())
                .user(plannerDto.getUser())
                .plannerTitle(plannerDto.getPlannerTitle())
                .createAt(plannerDto.getCreateAt())
                .updateAt(plannerDto.getUpdateAt())
                .day(plannerDto.getDay())
                .startDate(plannerDto.getStartDate())
                .endDate(plannerDto.getEndDate())
                .isPublic(plannerDto.isPublic())
                .description(plannerDto.getDescription())
                .area(plannerDto.getArea())
                .build();
    }


    // Planner 엔티티를 PlannerDto로 변환하는 생성자
    public PlannerDto(Planner planner,List<DestinationDto> destinations) {
        this.plannerID = planner.getPlannerID();
        this.plannerTitle = planner.getPlannerTitle();
        this.area = planner.getArea();
        this.day = planner.getDay();
        this.startDate = planner.getStartDate();
        this.endDate = planner.getEndDate();
        this.description = planner.getDescription();
        this.isPublic = planner.isPublic();
        this.destinations = destinations;
    }


}
