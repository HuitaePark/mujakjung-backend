package com.mujakjung.domain.attraction.restaurant.service;

import com.mujakjung.domain.attraction.restaurant.RestaurantMapper;
import com.mujakjung.domain.attraction.restaurant.repository.RestaurantRepository;
import com.mujakjung.domain.attraction.restaurant.dto.RestaurantApiResponse;
import com.mujakjung.domain.attraction.restaurant.entity.Restaurant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        String[] strArr = area.split(" ");
        String regionName   = strArr[0];

        Pageable pageable = PageRequest.of(0, 3);
        List<Restaurant> restaurant = restaurantRepository.findRegionRestaurants(regionName,pageable);

        return restaurantMapper.entityToDto(restaurant);
    }



}