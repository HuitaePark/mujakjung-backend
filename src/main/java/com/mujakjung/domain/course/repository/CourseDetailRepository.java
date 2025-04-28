package com.mujakjung.domain.course.repository;

import com.mujakjung.domain.course.Entity.CourseDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface CourseDetailRepository extends JpaRepository<CourseDetail,Long> {

    //세부 코스 목록 조회
    List<CourseDetail> findByCourseId(Long courseId);

}
