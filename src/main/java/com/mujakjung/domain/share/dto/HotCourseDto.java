package com.mujakjung.domain.share.dto;

import java.io.Serializable;
import java.util.List;


public record HotCourseDto(String courseName,
                           String region,
                           Double latitude,
                           Double longitude,
                           List<HotDetailCourseResponseDto> list)implements HotAttractionDto, Serializable {
}
