package com.mujakjung.domain.attraction.accommodation.service;

import com.mujakjung.domain.attraction.accommodation.entity.Accommodation;
import com.mujakjung.domain.attraction.accommodation.entity.AccommodationLike;
import com.mujakjung.domain.attraction.accommodation.repository.AccommodationLikeRepository;
import com.mujakjung.domain.attraction.accommodation.repository.AccommodationRepository;
import com.mujakjung.domain.member.Member;
import com.mujakjung.domain.member.MemberRepository;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Slf4j
public class AccommodationLikeService {
    private final MemberRepository memberRepository;
    private final AccommodationLikeRepository accommodationLikeRepository;
    private final AccommodationRepository accommodationRepository;


    public long likeAccommodation(Long accommodationId, String username) {
        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 유저입니다."));
        Long memberId = member.getId();

        // 새로운 리포지토리 메서드를 사용하여 이미 좋아요를 눌렀는지 확인
        if (accommodationLikeRepository.existsByAccommodation_IdAndMember_Id(accommodationId, memberId)) {
            return accommodationRepository.findById(accommodationId)
                    .map(Accommodation::getLikeCount)
                    .orElse(0);
        }

        Accommodation accommodation = accommodationRepository.findById(accommodationId)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 숙소입니다."));

        // 새로운 엔티티 구조에 맞게 builder 사용
        AccommodationLike accommodationLike = AccommodationLike.builder()
                .accommodation(accommodation)
                .member(member)
                .likedAt(Instant.now()) // LocalDateTime.now() -> Instant.now()
                .build();
        accommodationLikeRepository.save(accommodationLike);

        accommodationRepository.plusLikeCount(accommodationId);
        log.info("숙소 ID {}에 대해 회원 {}이(가) 좋아요를 눌렀습니다.", accommodationId, username);

        // DB와 동기화된 후의 likeCount는 +1 된 상태이므로 flush 없이 바로 반환 가능
        return accommodation.getLikeCount() + 1;
    }


    public boolean findLikeAccommodation(Long accommodationId, String username) {
        Long memberId = memberRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("찾을 수 없는 유저입니다."))
                .getId();
        // 새로운 리포지토리 메서드 사용
        return accommodationLikeRepository.existsByAccommodation_IdAndMember_Id(accommodationId, memberId);
    }


    public long getLikeCountAccommodation(Long accommodationId) {
        return accommodationRepository.findById(accommodationId)
                .map(Accommodation::getLikeCount)
                .orElse(0);
    }
}
