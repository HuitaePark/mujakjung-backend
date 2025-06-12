package com.mujakjung.domain.attraction.course.Entity;

import com.mujakjung.domain.member.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberDetailLike {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long courseDetailId;
    private Long memberId;

    @CreatedDate
    private LocalDateTime liked_At;

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩으로 성능 최적화
    @JoinColumn(name = "member_id")
    private Member member;


}
