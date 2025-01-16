package com.tripPlanner.project.domain.like;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tripPlanner.project.domain.signup.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDateTime;
@Entity
@Table(
        name = "tourist_like",
        indexes = {@Index(name = "idx_tourist_user", columnList = "touristId, userId")}
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TouristLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "touristId", nullable = false)
    private int touristId; // 관광지 ID (API에서 제공받은 관광지 ID)

    @Column(name = "userId", nullable = false)
    private String userId;

    @CreationTimestamp
    private LocalDateTime createdAt;
}