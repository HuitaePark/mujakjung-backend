package com.mujakjung.domain.attraction.course.repository;

import com.mujakjung.domain.attraction.course.Entity.Course;
import com.mujakjung.global.enums.MBTI;
import java.util.Optional;
import lombok.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query(value = "select c.name from Course c where c.id = :id")
    String findNameById(@Param("id")Long id);

    // 아무 코스나 랜덤으로 선택하는 메서드
    @Query(value = "SELECT a.id FROM Attraction a WHERE a.type = 'COURSE' ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Long findRandomCourseId();

    // MBTI로 랜덤 선택하는 메서드
    @Query(value = "SELECT a.id FROM Attraction a WHERE a.type = 'COURSE' AND a.mbti = :mbti ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Long findMbtiCourseId(@Param("mbti")String mbti);

    @Query(value = "SELECT a.id FROM Attraction a WHERE a.type = 'COURSE' AND a.region = :region ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Long findRegionCourseId(@Param("region")String region);

    @Query(value = "SELECT a.id FROM Attraction a WHERE a.type = 'COURSE' AND a.season = :season ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Long findSeasonCourseId(@Param("season")String season);

}
