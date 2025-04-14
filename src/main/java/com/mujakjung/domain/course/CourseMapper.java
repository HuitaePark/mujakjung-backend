package com.mujakjung.domain.course;

import com.mujakjung.domain.course.Entity.CourseDetail;
import com.mujakjung.domain.course.dto.DetailCourseResponseDto;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CourseMapper {

    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    List<DetailCourseResponseDto> courseToDto(List<CourseDetail> courseDetail);
}
