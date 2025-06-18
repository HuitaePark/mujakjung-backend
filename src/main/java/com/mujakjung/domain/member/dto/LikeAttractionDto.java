package com.mujakjung.domain.member.dto;

import com.mujakjung.domain.attraction.accommodation.entity.Accommodation;
import com.mujakjung.domain.attraction.course.Entity.CourseDetail;
import com.mujakjung.domain.attraction.restaurant.entity.Restaurant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class LikeAttractionDto {
    private List<CourseDto> courses;
    private List<RestaurantDto> restaurants;
    private List<AccommodationDto> accommodations;
}
