package com.mujakjung.domain.review;

import com.mujakjung.domain.attraction.course.Entity.CourseDetail;
import com.mujakjung.domain.attraction.course.repository.CourseDetailRepository;
import com.mujakjung.domain.review.dto.ReivewSaveDto;
import com.mujakjung.domain.review.dto.ReviewRequest;
import com.mujakjung.domain.review.dto.ReviewUpdateRequest;
import com.mujakjung.domain.review.dto.ReviewUpdatedto;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReivewSaveDto RequestToDto(ReviewRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "password")
    @Mapping(target = "create_date", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(source = "courseId", target = "courseDetail", qualifiedByName = "longToCourseDetail")
    Review toEntity(ReivewSaveDto dto,@Context CourseDetailRepository courseDetailRepository);

    @Named("longToCourseDetail")
    default CourseDetail longToCourseDetail(Long courseId, @Context CourseDetailRepository courseDetailRepository) {
        return courseDetailRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 코스입니다."));
    }

    ReviewUpdatedto updateRequestToDto(ReviewUpdateRequest updateRequest);
}