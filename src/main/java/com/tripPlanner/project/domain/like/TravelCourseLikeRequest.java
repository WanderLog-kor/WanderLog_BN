package com.tripPlanner.project.domain.like;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TravelCourseLikeRequest {
    private int travelCourseId;
    private String userId;
}
