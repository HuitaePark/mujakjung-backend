package com.mujakjung.domain.attraction.accommodation;

import com.mujakjung.domain.attraction.accommodation.dto.AccommodationApiResponse;
import com.mujakjung.domain.attraction.accommodation.dto.AccommodationUtil;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/accommodation")
public class AccommodationController {
    private final AccommodationService accommodationService;

    @GetMapping("/region")
    public ResponseEntity<AccommodationUtil> getAccommodation(@RequestParam String region){
        List<AccommodationApiResponse> list = accommodationService.findRegionAccommodation(region);
        AccommodationUtil response = new AccommodationUtil("Accommodation",list);
        return ResponseEntity.ok(response);
    }
}
