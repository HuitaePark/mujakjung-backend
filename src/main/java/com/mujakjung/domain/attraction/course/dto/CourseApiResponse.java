package com.mujakjung.domain.attraction.course.dto;

import java.util.List;

public record CourseApiResponse(String courseName,
                                String region,
                                Double latitude,
                                Double longitude,
                                List<DetailCourseResponseDto> list){
}
