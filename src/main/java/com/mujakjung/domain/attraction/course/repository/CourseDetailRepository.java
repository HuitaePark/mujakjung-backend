package com.mujakjung.domain.attraction.course.repository;

import com.mujakjung.domain.attraction.course.Entity.CourseDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;


public interface CourseDetailRepository extends JpaRepository<CourseDetail,Long> {

    //세부 코스 목록 조회
    List<CourseDetail> findByCourseId(Long courseId);

}
