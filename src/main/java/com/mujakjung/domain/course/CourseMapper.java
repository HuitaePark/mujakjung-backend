package com.mujakjung.domain.course;

import com.mujakjung.domain.course.Entity.CourseDetail;
import com.mujakjung.domain.course.dto.DetailCourseResponseDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "like", constant = "0") // 또는 다른 기본값
    DetailCourseResponseDto courseDetailToDto(CourseDetail courseDetail);

    List<DetailCourseResponseDto> courseToDto(List<CourseDetail> courseDetail);
}
