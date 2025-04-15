package com.mujakjung.domain.course.repository;

import com.mujakjung.domain.course.Entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course,Long> {

    @Query(value = "SELECT c.id FROM Course c "+
            "WHERE ST_Contains(ST_Buffer(POINT(:longitude, :latitude), 0.045), POINT(c.longitude, c.latitude)) "+
            "ORDER BY RAND() " +
            "LIMIT 1", nativeQuery = true)
    Long findRandomCourseIdNearby(@Param("longitude") double longitude,@Param("latitude") double latitude);
}
