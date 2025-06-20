package com.mujakjung.domain.attraction.restaurant.repository;

import com.mujakjung.domain.attraction.restaurant.entity.RestaurantLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RestaurantLikeRepository extends JpaRepository<RestaurantLike, Long> {
    // 엔티티를 로드하지 않고 ID를 통해 효율적으로 존재 여부를 확인합니다.
    boolean existsByRestaurant_IdAndMember_Id(Long restaurantId, Long memberId);

    long countByRestaurant_Id(Long restaurantId);
}