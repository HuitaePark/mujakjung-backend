package com.mujakjung.domain.attraction.course.repository;

import com.mujakjung.domain.attraction.course.Entity.CourseDetail;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CourseDetailRepository extends JpaRepository<CourseDetail,Long> {
    //세부 코스 목록 조회
    List<CourseDetail> findByCourseId(Long courseId);

    @Query(value = "UPDATE CourseDetail set likeCount = likeCount+1 where id = :id",nativeQuery = true)
    void plusLikeCount(@Param("id")Long DetailCourseId);
}
