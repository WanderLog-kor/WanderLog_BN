package com.tripPlanner.project.domain.like;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TouristLikeRequest {
    private int touristId;
    private String userId;

    private String contentid;

}
