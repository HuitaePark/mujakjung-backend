package com.mujakjung.domain.share.dto;

import com.mujakjung.global.enums.Region;
import java.io.Serializable;
import java.util.List;
import lombok.Builder;
import lombok.Getter;


public record HotCourseDto(String courseName,
                           Region region,
                           Double latitude,
                           Double longitude,
                           List<HotDetailCourseResponseDto> list)implements HotAttractionDto, Serializable {
}
