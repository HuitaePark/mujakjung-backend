package com.mujakjung.domain.course;

import com.mujakjung.domain.course.Entity.CourseDetail;
import com.mujakjung.domain.course.repository.CourseDetailRepository;
import com.mujakjung.domain.course.repository.CourseRepository;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseDetailRepository courseDetailRepository;

    /*
    /위도 경도를 받아서 코스를 조회하는 메서드
    */
    public Long findCourse(Double longitude, Double latitude) {
        return courseRepository.findRandomCourseIdNearby(longitude,latitude);
    }

    /*
    /코스를 받아서 세부 코스를 조회하는 메서드
    */
    public List<CourseDetail> findDetailCourse(Long courseId) {
        return courseDetailRepository.findByCourseId(courseId);
    }




}
