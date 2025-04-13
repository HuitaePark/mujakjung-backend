package com.mujakjung.domain.course.dto;

public record CourseResponseDto(String name,
                                String description,
                                String imgPath,
                                Integer like) {
}
