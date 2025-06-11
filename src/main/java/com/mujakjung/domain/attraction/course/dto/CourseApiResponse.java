package com.mujakjung.domain.attraction.course.dto;

import java.util.List;

public record CourseApiResponse(String courseName,
                                Long id,
                                String region,
                                Double latitude,
                                Double longitude,
                                String imgPath,
                                List<DetailCourseResponseDto> list,
                                int size){
}
