package com.mujakjung.domain.attraction.restaurant.dto;

public record RestaurantApiResponse(
        String imgPath,
        String name,
        Double latitude,
        Double longitude,
        String address,
        String websiteLink){
}
