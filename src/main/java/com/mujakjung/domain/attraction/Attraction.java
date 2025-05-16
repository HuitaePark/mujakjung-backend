package com.mujakjung.domain.attraction;

import com.mujakjung.global.enums.MBTI;
import com.mujakjung.global.enums.Region;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorColumn(name = "type")
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Attraction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgPath;
    private String name;
    private Double latitude;
    private Double longitude;

    private String region;

    @Enumerated(EnumType.STRING)
    private MBTI mbti;

    private String season;
}
