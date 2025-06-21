package com.mujakjung.domain.member.service;

import com.mujakjung.domain.attraction.accommodation.entity.AccommodationLike;
import com.mujakjung.domain.attraction.course.Entity.CourseDetailLike;
import com.mujakjung.domain.attraction.restaurant.entity.RestaurantLike;
import com.mujakjung.domain.member.Member;
import com.mujakjung.domain.member.MemberMapper;
import com.mujakjung.domain.member.MemberRepository;
import com.mujakjung.domain.member.dto.AccommodationDto;
import com.mujakjung.domain.member.dto.CourseDto;
import com.mujakjung.domain.member.dto.LikeAttractionDto;
import com.mujakjung.domain.member.dto.MypageDto;
import com.mujakjung.domain.member.dto.PasswordRequest;
import com.mujakjung.domain.member.dto.RestaurantDto;
import java.util.List;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MyPageService {
    private final MemberRepository memberRepository;
    private final MemberMapper mapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional(readOnly = true)
    public MypageDto getMyInfo(String username){
        Member member = memberRepository.findByUsername(username).orElseThrow(()-> new IllegalArgumentException("없는 유저 입니다."));
        return mapper.entityToDto(member);
    }

    @Transactional(readOnly = true)
    public void updatePassword(PasswordRequest passwordRequest, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new IllegalArgumentException("유저를 찾을수 없습니다."));
        if(member.checkPassword(passwordRequest.getCurrentPassword(), bCryptPasswordEncoder)){
           member.updatePassword(passwordRequest.getUpdatePassword(), bCryptPasswordEncoder);
           memberRepository.save(member);
        }
        else{
            throw new IllegalArgumentException("유저를 찾을수 없습니다.");
        }
    }

    @Transactional(readOnly = true)
    public LikeAttractionDto getMyLike(String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        List<CourseDto> likedCourses = member.getCourseDetailLikes().stream()
                .map(like -> CourseDto.from(like.getCourseDetail()))
                .toList();

        List<RestaurantDto> likedRestaurants = member.getRestaurantLikes().stream()
                .map(like -> RestaurantDto.from(like.getRestaurant()))
                .toList();

        List<AccommodationDto> likedAccommodations = member.getAccommodationLikes().stream()
                .map(like -> AccommodationDto.from(like.getAccommodation()))
                .toList();
        return new LikeAttractionDto(likedCourses, likedRestaurants, likedAccommodations);
    }
}
