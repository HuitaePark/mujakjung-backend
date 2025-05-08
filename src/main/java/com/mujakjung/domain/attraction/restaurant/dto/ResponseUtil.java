package com.mujakjung.domain.attraction.restaurant.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ResponseUtil {
    private String attract;
    private List<RestaurantApiResponse> list;
}
