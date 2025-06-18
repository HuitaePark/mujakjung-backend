package com.mujakjung.domain.member.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AttractionDto {
    private Long id;
    private String name;
    private String imgPath;
    private String region;
    private Double latitude;
    private Double longitude;
}