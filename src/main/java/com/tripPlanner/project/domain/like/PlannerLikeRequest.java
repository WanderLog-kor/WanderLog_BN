package com.tripPlanner.project.domain.like;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlannerLikeRequest {
    private int plannerID;
    private String userId;
}
