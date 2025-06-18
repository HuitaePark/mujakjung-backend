package com.mujakjung.domain.attraction.course.repository;

import com.mujakjung.domain.attraction.course.Entity.CourseDetailLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseDetailLikeRepository extends JpaRepository<CourseDetailLike,Long> {
    boolean existsByCourseDetailIdAndMember_Id(Long courseDetailId, Long memberId);
    long countByCourseDetailId(Long courseDetailId);
}
