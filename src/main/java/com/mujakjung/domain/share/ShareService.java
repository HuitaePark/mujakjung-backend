package com.mujakjung.domain.share;

import com.mujakjung.domain.attraction.accommodation.Accommodation;
import com.mujakjung.domain.attraction.accommodation.AccommodationRepository;
import com.mujakjung.domain.attraction.course.Entity.Course;
import com.mujakjung.domain.attraction.course.Entity.CourseDetail;
import com.mujakjung.domain.attraction.course.repository.CourseDetailRepository;
import com.mujakjung.domain.attraction.course.repository.CourseRepository;

import com.mujakjung.domain.attraction.restaurant.Restaurant;
import com.mujakjung.domain.attraction.restaurant.RestaurantRepository;
import com.mujakjung.domain.share.dto.HotAccommodationDto;
import com.mujakjung.domain.share.dto.HotAttractionDto;
import com.mujakjung.domain.share.dto.HotCourseDto;
import com.mujakjung.domain.share.dto.HotDetailCourseResponseDto;
import com.mujakjung.domain.share.dto.HotRestaurantDto;
import com.mujakjung.domain.share.dto.ShareDto;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ShareService {
    private final ShareRepository shareRepository;
    private final CourseRepository courseRepository;
    private final RestaurantRepository restaurantRepository;
    private final AccommodationRepository accommodationRepository;
    private final CourseDetailRepository courseDetailRepository;
    private final ShareMapper shareMapper;

    private final Map<String, Function<Long, HotAttractionDto>> handlerMap = new HashMap<>();

    @PostConstruct
    private void initFunctionMap() {
        handlerMap.put("COURSE", this::findHotCourse);
        handlerMap.put("ACCOMMODATION", this::findHotAccommodation);
        handlerMap.put("RESTAURANT", this::findHotRestaurant);
    }

    public void saveAttraction(ShareDto dto) {
        Share share = findAttraction(dto);
        shareRepository.save(share);
    }
    @Cacheable(cacheNames = "hot", key = "#type.toUpperCase()")
    public HotAttractionDto findHotAttraction(String type){

        //쉐어 테이블에서 원하는 타입을 카운트로 가장 많은 객체를 찾음
        Long attractionId = shareRepository.findTopAttractionIdByType(type);

        //type으로 맵 조회해서 메소드로 엔티티 반환
        Function<Long,HotAttractionDto> handler = handlerMap.get(type.toUpperCase());
        if (handler == null) {
            throw new IllegalArgumentException("지원하지 않는 타입입니다: " + type);
        }

        return handler.apply(attractionId);
    }




    private HotCourseDto findHotCourse(Long attractionId){
        List<CourseDetail> list = courseDetailRepository.findByCourseId(attractionId);
        List<HotDetailCourseResponseDto> courseList = shareMapper.courseToDto(list);
        Course course = courseRepository.findById(attractionId)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 코스"));
        return new HotCourseDto(course.getName(), course.getRegion(), course.getLatitude(), course.getLongitude(),
                course.getImgPath(), courseList);
    }

    private HotAccommodationDto findHotAccommodation(Long attractionId){
        Accommodation accommodation = accommodationRepository.findById(attractionId).orElseThrow(() -> new IllegalArgumentException("없는 숙소입니다."));
        return shareMapper.accommodationToDto(accommodation);
    }

    private HotRestaurantDto findHotRestaurant(Long attractionId){
        Restaurant restaurant = restaurantRepository.findById(attractionId).orElseThrow(()->new IllegalArgumentException("없는 식당입니다."));
        return shareMapper.restaurantToDto(restaurant);
    }

    private Share findAttraction(ShareDto dto){

        String type = dto.getType().toUpperCase();
        //디테일 코스를 공유했으므로 역방향으로 탐색
        if(type.equals("COURSE")){

        }

        return switch (type) {
            case "COURSE" -> {
                CourseDetail detail = courseDetailRepository.findById(dto.getId())
                        .orElseThrow(() -> new IllegalArgumentException("없는 디테일 코스"));
                Course course = detail.getCourse();
                yield Share.builder()
                        .attractionType("COURSE")
                        .attractionId(dto.getId())
                        .attractionName(course.getName())
                        .build();
            }
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
