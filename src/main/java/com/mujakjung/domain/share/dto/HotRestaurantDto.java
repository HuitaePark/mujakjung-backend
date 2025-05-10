package com.mujakjung.domain.share.dto;

import java.io.Serializable;

public record HotRestaurantDto(String imgPath,
                               String name,
                               Double latitude,
                               Double longitude,
                               String address,
                               String websiteLink)implements HotAttractionDto, Serializable {
}
