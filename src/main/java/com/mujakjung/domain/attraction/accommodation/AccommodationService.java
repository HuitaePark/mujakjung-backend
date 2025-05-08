package com.mujakjung.domain.attraction.accommodation;

import com.mujakjung.domain.attraction.accommodation.dto.AccommodationApiResponse;
import com.mujakjung.global.enums.Region;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AccommodationService {
    private final AccommodationRepository accommodationRepository;
    private final AccommodationMapper accommodationMapper;
    /*
        Region입력을 받아서 숙소를 조회하는 메서드
     */
    public List<AccommodationApiResponse> findRegionAccommodation(String area){
        Region region = Region.fromString(area);
        List<Accommodation> accommodations = accommodationRepository.findRegionAccommodations(region.getName());

        return accommodationMapper.entityToDto(accommodations);
    }
}
