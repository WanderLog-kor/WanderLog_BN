package com.tripPlanner.project.domain.makePlanner.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.tripPlanner.project.domain.makePlanner.dto.DestinationDto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@Table(name = "Destination")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Destination {

    @EmbeddedId
    private DestinationID destinationID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plannerID", referencedColumnName = "plannerID", insertable = false, updatable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonBackReference
    private Planner planner; // Planner와의 관계 설정 (외래 키)

    @Column(name = "name")
    private String name; // 장소명 (nullable)

    @Column(name = "x", nullable = false)
    private double x; // 경도

    @Column(name = "y", nullable = false)
    private double y; // 위도

    @Column(name = "address")
    private String address; // 주소

    @Column(name = "category")
    private String category; // 카테고리

    @Column(name = "image", length=1024)
    private String image; // 장소의 이미지 URL (구글 API사용)

    @Column(name = "uniqueid")
    private String uniqueId;

    public DestinationDto toDto() {
        return new DestinationDto(
                this.name,
                this.x,
                this.y,
                this.address,
                this.category,
                this.image,
//                "user1",  // Username만 포함
                this.planner.getUser().getUsername(),
                this.destinationID.getPlannerID(),
                this.destinationID.getDay(),
                this.destinationID.getDayOrder(),
                this.uniqueId
        );
    }
}