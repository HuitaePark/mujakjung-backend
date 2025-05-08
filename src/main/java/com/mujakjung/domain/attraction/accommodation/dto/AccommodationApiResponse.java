package com.mujakjung.domain.attraction.accommodation.dto;

public record AccommodationApiResponse(
        String imgPath,
        String name,
        Double latitude,
        Double longitude,
        String address,
        String websiteLink){
}