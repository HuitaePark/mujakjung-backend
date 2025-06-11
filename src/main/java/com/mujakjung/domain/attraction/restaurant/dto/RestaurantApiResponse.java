package com.mujakjung.domain.attraction.restaurant.dto;

public record RestaurantApiResponse(
        Long id,
        String imgPath,
        String name,
        Double latitude,
        Double longitude,
        String websiteLink){
}
