package com.mujakjung.domain.attraction.accommodation.entity;

import com.mujakjung.domain.attraction.Attraction;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DiscriminatorValue("ACCOMMODATION")
@Table(name = "accommodation")
public class Accommodation extends Attraction {
    private String websiteLink;

    @Column(name = "like_count", nullable = false)
    private int likeCount = 0;
}
