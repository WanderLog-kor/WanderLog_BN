package com.tripPlanner.project.domain.like;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.tripPlanner.project.domain.makePlanner.entity.Planner;
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
@Table(
        name = "planner_like",
        indexes = {@Index(name = "idx_planner_user", columnList = "plannerId, userId")}
)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlannerLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "plannerId", nullable = false)
    private int plannerId;

    @Column(name = "userId", nullable = false)
    private String userId;

    @CreationTimestamp
    private LocalDateTime createdAt;
}
