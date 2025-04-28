package com.mujakjung.domain.review;

import com.mujakjung.domain.review.dto.PasswordRequest;
import com.mujakjung.domain.review.dto.ReivewSaveDto;
import com.mujakjung.domain.review.dto.ReviewRequest;
import com.mujakjung.domain.review.dto.ReviewUpdateRequest;
import com.mujakjung.domain.review.dto.ReviewUpdatedto;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @PostMapping
    public ResponseEntity<?> saveReview(@RequestBody ReviewRequest request){
        //리퀘스트를 서비스 계층 dto로 변환
        ReivewSaveDto dto = reviewMapper.RequestToDto(request);
        //서비스의 saveReview메소드 호출
        reviewService.saveReview(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body("리뷰 생성 성공");
    }
    //리뷰삭제
    @DeleteMapping("/review/{id}")
    public ResponseEntity<?> deleteReview(@PathVariable Long id, @RequestBody PasswordRequest request){
        reviewService.passwordCheck(id,request.getPassword());
        reviewService.deleteReview(id);
        return ResponseEntity.noContent().build();
    }
    //리뷰 업데이트
    @PatchMapping("/review/{id}")
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestBody ReviewUpdateRequest updateRequest){
        reviewService.passwordCheck(id,updateRequest.getPassword());
        ReviewUpdatedto dto = reviewMapper.updateRequestToDto(updateRequest);
        reviewService.updateReview(id,dto);
        return ResponseEntity.status(HttpStatus.OK).body("리뷰 수정 성공");
    }

}
