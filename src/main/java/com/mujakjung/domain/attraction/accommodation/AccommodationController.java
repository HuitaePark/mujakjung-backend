package com.mujakjung.domain.attraction.accommodation;

import com.mujakjung.domain.attraction.accommodation.dto.AccommodationApiResponse;
import com.mujakjung.domain.attraction.accommodation.dto.AccommodationUtil;
import com.mujakjung.domain.attraction.accommodation.service.AccommodationLikeService;
import com.mujakjung.domain.attraction.accommodation.service.AccommodationService;
import com.mujakjung.domain.attraction.dto.LikeDto;
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
@RequestMapping("/accommodation")
public class AccommodationController {
    private final AccommodationService accommodationService;
    private final AccommodationLikeService accommodationLikeService;
    @GetMapping("/region")
    public ResponseEntity<AccommodationUtil> getAccommodation(@RequestParam String region){
        List<AccommodationApiResponse> list = accommodationService.findRegionAccommodation(region);
        AccommodationUtil response = new AccommodationUtil("Accommodation",list);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/{id}/like")
    public ResponseEntity<LikeDto> likeAccommodation(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        long updatedLikeCount = accommodationLikeService.likeAccommodation(id, username);
        // findLikeAccommodation은 likeAccommodation 실행 후 호출되므로 항상 true를 반환
        boolean likedStatus = true;
        LikeDto dto = new LikeDto(likedStatus, updatedLikeCount);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/{id}/like-status")
    public ResponseEntity<LikeDto> getAccommodationLikeStatus(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        boolean likedStatus = accommodationLikeService.findLikeAccommodation(id, username);
        long likeCount = accommodationLikeService.getLikeCountAccommodation(id);
        LikeDto dto = new LikeDto(likedStatus, likeCount);
        return ResponseEntity.ok(dto);
    }
}
