package com.mujakjung.domain.share.dto;

public record HotAccommodationDto(String imgPath,
                                  String name,
                                  Double latitude,
                                  Double longitude,
                                  String address,
                                  String websiteLink) implements HotAttractionDto{
}
