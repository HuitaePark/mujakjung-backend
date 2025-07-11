package com.mujakjung.domain.attraction.course.repository;

import com.mujakjung.domain.attraction.course.Entity.CourseDetail;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CourseDetailRepository extends JpaRepository<CourseDetail,Long> {
    //세부 코스 목록 조회
    List<CourseDetail> findByCourseId(Long courseId);

    @Modifying
    @Query("UPDATE CourseDetail cd SET cd.likeCount = cd.likeCount + 1 WHERE cd.id = :id")
    void plusLikeCount(@Param("id")Long DetailCourseId);

    Optional<CourseDetail> findCourseDetailById(Long id);
}
