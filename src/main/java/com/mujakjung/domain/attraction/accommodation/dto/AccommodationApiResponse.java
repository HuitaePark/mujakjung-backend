package com.mujakjung.domain.attraction.accommodation.dto;

public record AccommodationApiResponse(
        Long id,
        String imgPath,
        String name,
        Double latitude,
        Double longitude,
        String websiteLink){
}