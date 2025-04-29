package com.mujakjung.domain.share;

import com.mujakjung.domain.attraction.accommodation.AccmmodationRepository;
import com.mujakjung.domain.attraction.course.repository.CourseRepository;

import com.mujakjung.domain.attraction.restaurant.RestaurantRepository;
import com.mujakjung.domain.share.dto.ShareDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ShareService {
    private final ShareRepository shareRepository;
    private final CourseRepository courseRepository;
    private final RestaurantRepository restaurantRepository;
    private final AccmmodationRepository accommodationRepository;

    public void saveAttraction(ShareDto dto) {
        Share share = findAttraction(dto);
        shareRepository.save(share);
    }
    private Share findAttraction(ShareDto dto){

        String type = dto.getType();

        return switch (type) {
            case "COURSE" ->
                    Share.builder()
                    .attractionType("COURSE")
                    .attractionId(dto.getId())
                    .attractionName(courseRepository.findNameById(dto.getId()))
                    .build();
            case "ACCOMMODATION" -> Share.builder()
                    .attractionType("ACCOMMODATION")
                    .attractionId(dto.getId())
                    .attractionName(accommodationRepository.findNameById(dto.getId()))
                    .build();
            case "RESTAURANT" -> Share.builder()
                    .attractionType("RESTAURANT")
                    .attractionId(dto.getId())
                    .attractionName(restaurantRepository.findNameById(dto.getId()))
                    .build();
            default -> throw new IllegalStateException("Unexpected value: " + type);
        };
    }

}
