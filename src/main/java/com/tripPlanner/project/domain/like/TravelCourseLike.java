package com.tripPlanner.project.domain.like;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "travelcourse_like",
        indexes = {@Index(name = "idx_travelcourse_user", columnList = "travelcourseId, userId")}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelCourseLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "travelCourseId", nullable = false)
    private int travelCourseId; // 여행코스 ID (API에서 제공받은 관광지 ID)

    @Column(name = "userId", nullable = false)
    private String userId;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
