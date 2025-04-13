package com.mujakjung.domain.course;

import com.mujakjung.domain.course.Entity.Course;
import com.mujakjung.domain.course.Entity.CourseDetail;
import java.util.ArrayList;
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

    @GetMapping
    public ResponseEntity<?> findCourse(@RequestParam Double latitude, Double longitude){

        //먼저 코스를 조회
        Long courseId = courseService.findCourse(latitude,longitude);
        //그다음 코스에 딸린 세부코스를 조회
        List<CourseDetail> list = courseService.findDetailCourse(courseId);
        //dto에 담아서 리턴

        return ResponseEntity.ok("조회 완료");
    }
}
