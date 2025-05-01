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
    @GetMapping("/random")
    public ResponseEntity<CourseApiResponse> findRandomCourse() {
        CourseApiResponse apiResponse = courseService.findCourse();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/mbti")
    public ResponseEntity<CourseApiResponse> findMbtiCourse(@RequestParam String mbti){

    }


    @GetMapping("/region")
    public ResponseEntity<CourseApiResponse> findRegoinCourse(@RequestParam String Region){

    }

    @GetMapping("/summer")
    public ResponseEntity<CourseApiResponse> findSummerCourse(){

    }

    @GetMapping("/winter")
    public ResponseEntity<CourseApiResponse> findWinterCourse(){

    }


}
