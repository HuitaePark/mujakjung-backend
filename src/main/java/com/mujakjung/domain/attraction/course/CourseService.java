package com.mujakjung.domain.attraction.course;

import com.mujakjung.domain.attraction.course.Entity.Course;
import com.mujakjung.domain.attraction.course.Entity.CourseDetail;
import com.mujakjung.domain.attraction.course.dto.CourseApiResponse;
import com.mujakjung.domain.attraction.course.dto.DetailCourseResponseDto;
import com.mujakjung.domain.attraction.course.repository.CourseDetailRepository;
import com.mujakjung.domain.attraction.course.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseDetailRepository courseDetailRepository;

    /*
        위도 경도를 받아서 코스를 조회하는 메서드
    */
    public Long findCourse(Double longitude, Double latitude) {
        return courseRepository.findRandomCourseIdNearby(longitude, latitude);
    }

    /*
        코스를 받아서 세부 코스를 조회하는 메서드
    */
    public List<CourseDetail> findDetailCourse(Long courseId) {
        return courseDetailRepository.findByCourseId(courseId);
    }

    /*
        받은 세부코스들로 Response를 생성하는 메서드
    */
    public CourseApiResponse makeResponse(Long id, List<DetailCourseResponseDto> list) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 코스"));
        String courseName = course.getName();
        String region = course.getRegion();
        return new CourseApiResponse(courseName, region, list);
    }
}
