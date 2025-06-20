package com.mujakjung.domain.share.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serial;

@JsonTypeName("restaurant")
public record HotRestaurantDto(String imgPath,
                               String name,
                               Double latitude,
                               Double longitude,
                               String address,
                               String websiteLink) implements HotAttractionDto{
    @Serial
    private static final long serialVersionUID = 1L;
}
