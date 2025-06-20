package com.mujakjung.domain.attraction.accommodation.repository;

import com.mujakjung.domain.attraction.accommodation.entity.AccommodationLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccommodationLikeRepository extends JpaRepository<AccommodationLike, Long> {
    // 엔티티를 로드하지 않고 ID를 통해 효율적으로 존재 여부를 확인합니다.
    boolean existsByAccommodation_IdAndMember_Id(Long accommodationId, Long memberId);

    // 카운트가 필요할 경우를 대비한 메서드
    long countByAccommodation_Id(Long accommodationId);
}