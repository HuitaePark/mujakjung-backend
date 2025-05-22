package com.mujakjung.domain.attraction.course.dto;

import com.mujakjung.domain.review.Review;
import java.util.List;

public record DetailCourseResponseDto(String name,
                                      String description,
                                      String imgPath,
                                      Integer like) {
}
