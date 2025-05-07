package com.mujakjung.domain.attraction.restaurant;

import com.mujakjung.domain.attraction.restaurant.dto.ResponseUtil;
import com.mujakjung.domain.attraction.restaurant.dto.RestaurantApiResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;

    @GetMapping("/region")
    public ResponseEntity<ResponseUtil> getRestaurant(@RequestParam String region){
        List<RestaurantApiResponse> list = restaurantService.findRegionRestaurants(region);
        ResponseUtil response = new ResponseUtil("Restaurant",list);
        return ResponseEntity.ok(response);
    }
}
