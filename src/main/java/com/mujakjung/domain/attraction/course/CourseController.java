package com.mujakjung.domain.attraction.course;

import com.mujakjung.domain.attraction.course.Entity.CourseDetail;
import com.mujakjung.domain.attraction.course.dto.CourseApiResponse;
import com.mujakjung.domain.attraction.course.dto.DetailCourseResponseDto;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/course")
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;


    //모두 랜덤 조회
    @GetMapping
    public ResponseEntity<?> findCourse(@RequestParam Double latitude, Double longitude) {

        //먼저 코스를 조회
        Long courseId = courseService.findCourse(latitude, longitude);
        //그다음 코스에 딸린 세부코스를 조회
        List<CourseDetail> list = courseService.findDetailCourse(courseId);
        //dto에 담는다
        List<DetailCourseResponseDto> courseList = courseMapper.courseToDto(list);
        //Response 생성
        CourseApiResponse apiResponse = courseService.makeResponse(courseId,courseList);
        return ResponseEntity.ok(apiResponse);
    }
}
