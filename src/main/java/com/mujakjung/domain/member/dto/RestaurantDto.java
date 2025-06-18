package com.mujakjung.domain.member.dto;

import com.mujakjung.domain.attraction.restaurant.entity.Restaurant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class RestaurantDto extends AttractionDto{
    private String websiteLink;

    public RestaurantDto(Long id, String name, String imgPath, String region,
                         Double latitude, Double longitude, String websiteLink) {
        super(id, name, imgPath, region, latitude, longitude);
        this.websiteLink = websiteLink;
    }

    public static RestaurantDto from(Restaurant restaurant) {
        return new RestaurantDto(
                restaurant.getId(),
                restaurant.getName(),
                restaurant.getImgPath(),
                restaurant.getRegion(),
                restaurant.getLatitude(),
                restaurant.getLongitude(),
                restaurant.getWebsiteLink()
        );
    }
}
