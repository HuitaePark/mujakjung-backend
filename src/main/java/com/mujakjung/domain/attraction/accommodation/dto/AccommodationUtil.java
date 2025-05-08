package com.mujakjung.domain.attraction.accommodation.dto;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AccommodationUtil {
    private String attract;
    private List<AccommodationApiResponse> list;
}
