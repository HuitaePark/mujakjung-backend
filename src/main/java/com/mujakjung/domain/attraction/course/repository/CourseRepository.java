package com.mujakjung.domain.attraction.course.repository;

import com.mujakjung.domain.attraction.course.Entity.Course;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(value = "SELECT c.id FROM Course c " +
            "WHERE ST_Contains(ST_Buffer(POINT(:longitude, :latitude), 0.045), POINT(c.longitude, c.latitude)) " +
            "ORDER BY RAND() " +
            "LIMIT 1", nativeQuery = true)
    Long findRandomCourseIdNearby(@Param("longitude") double longitude, @Param("latitude") double latitude);

    Optional<Course> findById(Long id);

    @Query(value = "select c.name from Course c where c.id = :id")
    String findNameById(@Param("id")Long id);
}
