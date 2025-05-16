package com.mujakjung.domain.attraction.restaurant;

import com.mujakjung.domain.attraction.restaurant.dto.RestaurantApiResponse;
import java.util.List;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RestaurantMapper {

    List<RestaurantApiResponse> entityToDto(List<Restaurant> list);
}
