package com.mujakjung.domain.share.dto;

import com.fasterxml.jackson.annotation.JsonTypeName;
import java.io.Serial;
import java.io.Serializable;
@JsonTypeName("accommodation")
public record HotAccommodationDto(String imgPath,
                                  String name,
                                  Double latitude,
                                  Double longitude,
                                  String address,
                                  String websiteLink) implements HotAttractionDto, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
