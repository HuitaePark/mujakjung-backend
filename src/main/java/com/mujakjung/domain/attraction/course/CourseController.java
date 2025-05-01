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


    //모두 랜덤 조회
    @GetMapping("/random")
    public ResponseEntity<CourseApiResponse> findRandomCourse() {
        CourseApiResponse apiResponse = courseService.findCourse();
        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/mbti")
    public ResponseEntity<CourseApiResponse> findMbtiCourse(@RequestParam String type){
        CourseApiResponse apiResponse = courseService.findMbtiCourse(type);
        return ResponseEntity.ok(apiResponse);
    }


    @GetMapping("/region")
    public ResponseEntity<CourseApiResponse> findRegoinCourse(@RequestParam String region){
        CourseApiResponse apiResponse = courseService.findRegionCourse(region);
        return ResponseEntity.ok(apiResponse);
    }


    @GetMapping("/season")
    public ResponseEntity<CourseApiResponse> findSummerCourse(@RequestParam String season){
        CourseApiResponse apiResponse = courseService.findSummerCourse(season);
        return ResponseEntity.ok(apiResponse);
    }


}
