package com.mujakjung.domain.share.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serial;
import java.io.Serializable;
import java.util.List;
import lombok.Builder;

@Builder
@JsonTypeName("course")
public record HotCourseDto(String courseName,
                           String detailName,
                           String region,
                           int likeCount,
                           String imgPath,
                           String description)implements HotAttractionDto, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
