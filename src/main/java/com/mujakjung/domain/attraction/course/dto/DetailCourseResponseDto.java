package com.mujakjung.domain.attraction.course.dto;


public record DetailCourseResponseDto(Long id,
                                      String name,
                                      String description,
                                      String imgPath,
                                      Integer likeCount) {
}
