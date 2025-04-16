package com.mujakjung.domain.course.dto;

import java.util.List;

public record CourseApiResponse(String region,
                                String courseName,
                                List<DetailCourseResponseDto> list){
}
