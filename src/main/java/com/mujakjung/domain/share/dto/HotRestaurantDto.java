package com.mujakjung.domain.share.dto;

public record HotRestaurantDto(String imgPath,
                               String name,
                               Double latitude,
                               Double longitude,
                               String address,
                               String websiteLink)implements HotAttractionDto{
}
