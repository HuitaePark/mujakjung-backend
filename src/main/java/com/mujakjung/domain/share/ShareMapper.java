package com.mujakjung.domain.share;

import com.mujakjung.domain.attraction.course.Entity.CourseDetail;
import com.mujakjung.domain.share.dto.HotDetailCourseResponseDto;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShareMapper {
    List<HotDetailCourseResponseDto> courseToDto(List<CourseDetail> list);
}
