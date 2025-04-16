package com.mujakjung.domain.course.Entity;

import com.mujakjung.domain.review.Review;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Builder
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CourseDetail {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    private String name;
    private String description;
    private String imgPath;
    private Integer likeCount;

    @ManyToOne(fetch = FetchType.LAZY) // 지연 로딩으로 성능 최적화
    @JoinColumn(name = "course_id")
    private Course course;

    @OneToMany(mappedBy = "courseDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20) // N+1 문제 완화를 위한 배치 로딩 설정
    private List<Review> reviews = new ArrayList<>();
}
