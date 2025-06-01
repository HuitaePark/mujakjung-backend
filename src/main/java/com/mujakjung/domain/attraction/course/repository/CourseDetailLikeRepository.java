package com.mujakjung.domain.attraction.course.repository;

import com.mujakjung.domain.attraction.course.Entity.CourseDetailLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseDetailLikeRepository extends JpaRepository<CourseDetailLike,Long> {
    boolean existsByCourseDetailIdAndIp(Long courseDetailId, String ip);
    long countByCourseDetailId(Long courseDetailId);
}
