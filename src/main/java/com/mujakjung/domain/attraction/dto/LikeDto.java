package com.mujakjung.domain.attraction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeDto {
    boolean liked;
    long totalLikes;
}
