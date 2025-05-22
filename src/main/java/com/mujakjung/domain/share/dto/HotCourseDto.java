package com.mujakjung.domain.share.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serializable;
import java.util.List;

@JsonTypeName("course")
public record HotCourseDto(String courseName,
                           String region,
                           Double latitude,
                           Double longitude,
                           String imgPath,
                           List<HotDetailCourseResponseDto> list)implements HotAttractionDto, Serializable {
}
