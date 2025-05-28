package com.mujakjung.domain.share.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record HotDetailCourseResponseDto(
                                        Long id,
                                        String name,
                                        String description,
                                        String imgPath,
                                        Integer likeCount) {
}
