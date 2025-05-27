package com.mujakjung.domain.attraction.course.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CourseLikeDto {
    boolean liked;
    long totalLikes;
}
