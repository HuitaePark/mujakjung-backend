package com.mujakjung.domain.attraction.course;

import com.mujakjung.domain.attraction.course.dto.CourseApiResponse;
import com.mujakjung.domain.attraction.dto.LikeDto;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/{id}/like")
    public ResponseEntity<LikeDto> like(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        long updatedLikeCount = courseService.likeCourse(id, username); // 업데이트된 좋아요 수 반환 받음
        boolean likedStatus = courseService.findLikeCourse(id, username);
        LikeDto dto = new LikeDto(likedStatus, updatedLikeCount);
        return ResponseEntity.ok(dto);
    }
    @GetMapping("/{id}/like-status")
    public ResponseEntity<?> getLikeStatus(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        LikeDto dto = new LikeDto(courseService.findLikeCourse(id,username),courseService.getLikeCount(id));
        return ResponseEntity.ok(dto);
    }

}
