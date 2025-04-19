package com.mujakjung.domain.review;

import com.mujakjung.domain.review.dto.ReivewSaveDto;
import com.mujakjung.domain.review.dto.ReviewRequest;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;


@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReivewSaveDto RequestToDto(ReviewRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "password", source = "password")
    @Mapping(target = "create_date", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "courseDetail", ignore = true)
    Review toEntity(ReivewSaveDto dto);
}
