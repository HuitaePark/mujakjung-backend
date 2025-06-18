package com.mujakjung.domain.share;

import com.mujakjung.domain.attraction.accommodation.entity.Accommodation;
import com.mujakjung.domain.attraction.course.Entity.CourseDetail;
import com.mujakjung.domain.attraction.restaurant.entity.Restaurant;
import com.mujakjung.domain.share.dto.HotAccommodationDto;
import com.mujakjung.domain.share.dto.HotDetailCourseResponseDto;
import com.mujakjung.domain.share.dto.HotRestaurantDto;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ShareMapper {
    List<HotDetailCourseResponseDto> courseToDto(List<CourseDetail> list);

    HotAccommodationDto accommodationToDto(Accommodation accommodation);

    HotRestaurantDto restaurantToDto(Restaurant restaurant);
}
