package com.mujakjung.domain.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review,Long> {
    @Query("SELECT r FROM Review r WHERE r.courseDetail.id = :courseId")
    Page<Review> findbyCourseDetailId(Pageable pageable,@Param("courseId")Long courseId);
}
