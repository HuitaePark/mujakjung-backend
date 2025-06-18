package com.mujakjung.domain.attraction.accommodation;

import com.mujakjung.domain.attraction.accommodation.dto.AccommodationApiResponse;
import com.mujakjung.domain.attraction.accommodation.entity.Accommodation;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccommodationMapper {
    List<AccommodationApiResponse> entityToDto(List<Accommodation> list);
}
