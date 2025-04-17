package com.mujakjung.domain.review;

import com.mujakjung.domain.review.dto.ReivewSaveDto;
import com.mujakjung.domain.review.dto.ReviewRequest;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface ReviewMapper {

    ReivewSaveDto RequestToDto(ReviewRequest request);

    Review toEntity(ReivewSaveDto dto);
}
