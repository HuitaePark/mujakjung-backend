package com.mujakjung.domain.attraction.accommodation;

import com.mujakjung.domain.attraction.Attraction;
import com.mujakjung.domain.attraction.accommodation.dto.AccommodationApiResponse;
import com.mujakjung.global.enums.Region;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
        Pageable pageable = PageRequest.of(0, 3);
        List<Accommodation> accommodations = accommodationRepository.findRegionAccommodations(area,pageable);

        return accommodationMapper.entityToDto(accommodations);
    }
}
