package com.mujakjung.domain.attraction.restaurant.service;

import com.mujakjung.domain.attraction.restaurant.entity.Restaurant;
import com.mujakjung.domain.attraction.restaurant.entity.RestaurantLike;
import com.mujakjung.domain.attraction.restaurant.repository.RestaurantLikeRepository;
import com.mujakjung.domain.attraction.restaurant.repository.RestaurantRepository;
import com.mujakjung.domain.member.Member;
import com.mujakjung.domain.member.MemberRepository;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
@Slf4j
public class RestaurantLikeService {

    private final MemberRepository memberRepository;
    private final RestaurantLikeRepository restaurantLikeRepository;
    private final RestaurantRepository restaurantRepository;

    @Transactional
    public long likeRestaurant(Long restaurantId, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 유저입니다."));
        Long memberId = member.getId();

        if (restaurantLikeRepository.existsByRestaurant_IdAndMember_Id(restaurantId, memberId)) {
            return restaurantRepository.findById(restaurantId)
                    .map(Restaurant::getLikeCount)
                    .orElse(0);
        }

        Restaurant restaurant = restaurantRepository.findById(restaurantId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 맛집입니다."));

        RestaurantLike restaurantLike = RestaurantLike.builder()
                .restaurant(restaurant)
                .member(member)
                .likedAt(Instant.now())
                .build();
        restaurantLikeRepository.save(restaurantLike);

        restaurantRepository.plusLikeCount(restaurantId);
        log.info("맛집 ID {}에 대해 회원 {}이(가) 좋아요를 눌렀습니다.", restaurantId, username);

        return restaurant.getLikeCount() + 1;
    }

    @Transactional(readOnly = true)
    public boolean findLikeRestaurant(Long restaurantId, String username) {
        Long memberId = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 유저입니다."))
                .getId();
        return restaurantLikeRepository.existsByRestaurant_IdAndMember_Id(restaurantId, memberId);
    }

    @Transactional(readOnly = true)
    public long getLikeCountRestaurant(Long restaurantId) {
        return restaurantRepository.findById(restaurantId)
                .map(Restaurant::getLikeCount)
                .orElse(0);
    }
}
