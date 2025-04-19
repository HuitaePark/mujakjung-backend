package com.mujakjung.domain.course.Entity;


import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

@Getter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imgPath;
    private String courseName;
    private Double latitude;
    private Double longitude;
    private String region;

    // 세부 항목과의 관계 (지연 로딩 사용)
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20) // N+1 문제 완화를 위한 배치 로딩 설정
    private final List<CourseDetail> details = new ArrayList<>();

}
