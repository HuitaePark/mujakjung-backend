package com.mujakjung.domain.share;

import com.mujakjung.domain.attraction.accommodation.entity.Accommodation;
import com.mujakjung.domain.attraction.accommodation.repository.AccommodationRepository;
import com.mujakjung.domain.attraction.course.Entity.Course;
import com.mujakjung.domain.attraction.course.Entity.CourseDetail;
import com.mujakjung.domain.attraction.course.repository.CourseDetailRepository;
import com.mujakjung.domain.attraction.course.repository.CourseRepository;

import com.mujakjung.domain.attraction.restaurant.entity.Restaurant;
import com.mujakjung.domain.attraction.restaurant.repository.RestaurantRepository;
import com.mujakjung.domain.share.dto.HotAccommodationDto;
import com.mujakjung.domain.share.dto.HotAttractionDto;
import com.mujakjung.domain.share.dto.HotCourseDto;
import com.mujakjung.domain.share.dto.HotRestaurantDto;
import com.mujakjung.domain.share.dto.ShareDto;
import jakarta.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
    public void saveAttraction(ShareDto dto) {
        Share share = findAttraction(dto);
        shareRepository.save(share);
    }
    @Transactional(readOnly = true)
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



    @Transactional(readOnly = true)
    protected HotCourseDto findHotCourse(Long attractionId){
        CourseDetail detail = courseDetailRepository
                .findById(attractionId)
                .orElseThrow(() -> new IllegalArgumentException("없는 코스입니다."));

        Course course = detail.getCourse();      // 부모 코스




        return HotCourseDto.builder()          // ← @Builder 를 쓰지 않는다면 new HotCourseDto( … )
                .courseName(course.getName())  // 코스 이름
                .detailName(detail.getName())  // 디테일 이름
                .region(course.getRegion())    // 지역(코스 기준)
                .likeCount(detail.getLikeCount())          // 총 좋아요 수
                .imgPath(detail.getImgPath())  // 이미지
                .description(detail.getDescription()) // 설명
                .build();
    }

    @Transactional(readOnly = true)
    protected HotAccommodationDto findHotAccommodation(Long attractionId){
        Accommodation accommodation = accommodationRepository.findById(attractionId).orElseThrow(() -> new IllegalArgumentException("없는 숙소입니다."));
        return shareMapper.accommodationToDto(accommodation);
    }

    @Transactional(readOnly = true)
    protected HotRestaurantDto findHotRestaurant(Long attractionId){
        Restaurant restaurant = restaurantRepository.findById(attractionId).orElseThrow(()->new IllegalArgumentException("없는 식당입니다."));
        return shareMapper.restaurantToDto(restaurant);
    }

    @Transactional(readOnly = true)
    protected Share findAttraction(ShareDto dto){

        String type = dto.getType().toUpperCase();


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
