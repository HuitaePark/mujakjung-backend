package com.mujakjung.domain.attraction.restaurant;

import com.mujakjung.domain.attraction.dto.LikeDto;
import com.mujakjung.domain.attraction.restaurant.dto.ResponseUtil;
import com.mujakjung.domain.attraction.restaurant.dto.RestaurantApiResponse;
import com.mujakjung.domain.attraction.restaurant.service.RestaurantLikeService;
import com.mujakjung.domain.attraction.restaurant.service.RestaurantService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/restaurant")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final RestaurantLikeService restaurantLikeService;

    @GetMapping("/region")
    public ResponseEntity<ResponseUtil> getRestaurant(@RequestParam String region){
        List<RestaurantApiResponse> list = restaurantService.findRegionRestaurants(region);
        ResponseUtil response = new ResponseUtil("Restaurant",list);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<LikeDto> likeRestaurant(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        long updatedLikeCount = restaurantLikeService.likeRestaurant(id, username);
        boolean likedStatus = true; // likeRestaurant 실행 후에는 항상 좋아요 상태
        LikeDto dto = new LikeDto(likedStatus, updatedLikeCount);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/like-status")
    public ResponseEntity<LikeDto> getRestaurantLikeStatus(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        boolean likedStatus = restaurantLikeService.findLikeRestaurant(id, username);
        long likeCount = restaurantLikeService.getLikeCountRestaurant(id);
        LikeDto dto = new LikeDto(likedStatus, likeCount);
        return ResponseEntity.ok(dto);
    }

}
