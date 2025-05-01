package com.mujakjung.domain.attraction.course;

import com.mujakjung.domain.attraction.course.Entity.Course;
import com.mujakjung.domain.attraction.course.Entity.CourseDetail;
import com.mujakjung.domain.attraction.course.dto.CourseApiResponse;
import com.mujakjung.domain.attraction.course.dto.DetailCourseResponseDto;
import com.mujakjung.domain.attraction.course.repository.CourseDetailRepository;
import com.mujakjung.domain.attraction.course.repository.CourseRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@AllArgsConstructor
@Service
public class CourseService {
    private final CourseRepository courseRepository;
    private final CourseDetailRepository courseDetailRepository;
    private final CourseMapper courseMapper;

    /*
       랜덤 코스 조회 카테고리 메서드
    */
    public CourseApiResponse findCourse() {
         //랜덤으로 아무 코스나 선택
        Long courseId = courseRepository.findRandomCourseId();
        log.info("랜덤 코스 선택 결과: {}", courseId);
        //코스 디테일 랜덤조회한 코스아이디로 조회
        List<CourseDetail> list = findDetailCourse(courseId);
        //dto들의 리스트로 변환후 json으로 만들어서 반환
        List<DetailCourseResponseDto> courseList = courseMapper.courseToDto(list);
        return makeResponse(courseId, courseList);
    }
    








    /*
        코스를 받아서 세부 코스를 조회하는 메서드
    */
    private List<CourseDetail> findDetailCourse(Long courseId) {
        return courseDetailRepository.findByCourseId(courseId);
    }

    /*
        받은 세부코스들로 Response를 생성하는 메서드
    */
    private CourseApiResponse makeResponse(Long id, List<DetailCourseResponseDto> list) {

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("존재하지 않는 코스"));

        return new CourseApiResponse(course.getName(), course.getRegion(), course.getLatitude(), course.getLongitude(), list);
    }
}
