package com.mujakjung.domain.attraction.course.Entity;

import com.mujakjung.domain.member.Member;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "course_detail_like",
        uniqueConstraints = @UniqueConstraint(name = "ux_detail_ip",
                columnNames = {"course_detail_id", "ip"}))
public class CourseDetailLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "course_detail_id", nullable = false)
    private Long courseDetailId;


    @Column(nullable = false, length = 45)
    private String ip;

    @CreatedDate
    @Column(name = "liked_at", nullable = false)
    private LocalDateTime likedAt;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
}
