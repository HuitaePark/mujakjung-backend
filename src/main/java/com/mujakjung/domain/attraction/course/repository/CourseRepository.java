package com.mujakjung.domain.attraction.course.repository;

import com.mujakjung.domain.attraction.course.Entity.Course;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(value = "select c.name from Course c where c.id = :id")
    String findNameById(@Param("id")Long id);

    // 아무 코스나 랜덤으로 선택하는 백업 메서드
    @Query(value = "SELECT a.id FROM Attraction a WHERE a.type = 'COURSE' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Long findRandomCourseId();
}
