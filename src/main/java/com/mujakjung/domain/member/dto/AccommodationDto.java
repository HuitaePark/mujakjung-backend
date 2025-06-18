package com.mujakjung.domain.member.dto;

import com.mujakjung.domain.attraction.accommodation.entity.Accommodation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationDto extends AttractionDto{
    private String websiteLink;

    public AccommodationDto(Long id, String name, String imgPath, String region,
                            Double latitude, Double longitude, String websiteLink) {
        super(id, name, imgPath, region, latitude, longitude);
        this.websiteLink = websiteLink;
    }

    public static AccommodationDto from(Accommodation acc) {
        return new AccommodationDto(
                acc.getId(),
                acc.getName(),
                acc.getImgPath(),
                acc.getRegion(),
                acc.getLatitude(),
                acc.getLongitude(),
                acc.getWebsiteLink()
        );
    }
}
