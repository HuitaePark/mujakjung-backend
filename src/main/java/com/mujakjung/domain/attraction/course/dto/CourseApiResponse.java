package com.mujakjung.domain.attraction.course.dto;

import com.mujakjung.global.enums.Region;
import java.util.List;

public record CourseApiResponse(String courseName,
                                Region region,
                                Double latitude,
                                Double longitude,
                                List<DetailCourseResponseDto> list){
}
