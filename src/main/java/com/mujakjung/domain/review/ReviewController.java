package com.mujakjung.domain.review;

import com.mujakjung.domain.review.dto.ReivewSaveDto;
import com.mujakjung.domain.review.dto.ReviewRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/review")

public class ReviewController {

    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @PostMapping()
    public ResponseEntity<?> saveReview(@RequestBody ReviewRequest request){
        //리퀘스트를 서비스 계층 dto로 변환
        ReivewSaveDto dto = reviewMapper.RequestToDto(request);
        //서비스의 saveReview메소드 호출
        reviewService.saveReview(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("리뷰 생성 성공");
    }
}
