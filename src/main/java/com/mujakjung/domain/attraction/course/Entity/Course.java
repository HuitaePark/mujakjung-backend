package com.mujakjung.domain.attraction.course.Entity;

import com.mujakjung.domain.attraction.Attraction;
import jakarta.persistence.CascadeType;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.hibernate.annotations.BatchSize;

@Getter
@Entity
@Builder
@DiscriminatorValue("COURSE")
@AllArgsConstructor
@Table(name = "course")
public class Course extends Attraction {

    // 세부 항목과의 관계 (지연 로딩 사용)
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @BatchSize(size = 20) // N+1 문제 완화를 위한 배치 로딩 설정
    private final List<CourseDetail> details = new ArrayList<>();

}
