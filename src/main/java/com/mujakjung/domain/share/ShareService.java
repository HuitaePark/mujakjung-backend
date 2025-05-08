package com.mujakjung.domain.share;

import com.mujakjung.domain.attraction.accommodation.AccommodationRepository;
import com.mujakjung.domain.attraction.course.repository.CourseRepository;

import com.mujakjung.domain.attraction.restaurant.RestaurantRepository;
import com.mujakjung.domain.share.dto.HotAttractionDto;
import com.mujakjung.domain.share.dto.ShareDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ShareService {
    private final ShareRepository shareRepository;
    private final CourseRepository courseRepository;
    private final RestaurantRepository restaurantRepository;
    private final AccommodationRepository accommodationRepository;

    public void saveAttraction(ShareDto dto) {
        Share share = findAttraction(dto);
        shareRepository.save(share);
    }
    public HotAttractionDto findHotAttraction(){
        //쉐어 테이블에서 카운트로 가장 많은 객체를 찾음

        //찾은 객체들을 각 테이블 가서 조회함
        //dto로 변환후 컨트롤러로 전달
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
