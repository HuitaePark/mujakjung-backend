package com.mujakjung.domain.attraction.restaurant;

import com.mujakjung.domain.attraction.restaurant.dto.RestaurantApiResponse;
import com.mujakjung.global.enums.Region;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class RestaurantService {
    private final RestaurantRepository restaurantRepository;
    private final RestaurantMapper restaurantMapper;

    /*
        Region입력을 받아서 레스토랑을 조회하는 메서드
     */
    public List<RestaurantApiResponse> findRegionRestaurants(String area) {
        Region region = Region.fromString(area);
        List<Restaurant> restaurant = restaurantRepository.findRegionRestaurants(region.getName());

        return restaurantMapper.entityToDto(restaurant);
    }



}