package com.mujakjung.domain.attraction.course.dto;

import com.mujakjung.domain.review.Review;
import java.util.List;

public record DetailCourseResponseDto(Long id,
                                      String name,
                                      String description,
                                      String imgPath,
                                      Integer like) {
}
