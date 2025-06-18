package com.mujakjung.domain.member.controller;

import com.mujakjung.domain.member.dto.LikeAttractionDto;
import com.mujakjung.domain.member.dto.MypageDto;
import com.mujakjung.domain.member.dto.PasswordRequest;
import com.mujakjung.domain.member.service.MyPageService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/my")
public class MyPageController {
    private final MyPageService myPageService;

    @GetMapping
    public ResponseEntity<?> getMyInfo(@AuthenticationPrincipal UserDetails userDetails){
        MypageDto mypageDto = myPageService.getMyInfo(userDetails.getUsername());
           return ResponseEntity.ok(mypageDto);
    }
    @PatchMapping("/password")
    public ResponseEntity<?> updateMyPassword(@AuthenticationPrincipal UserDetails userDetails, @RequestBody PasswordRequest passwordRequest) {
        myPageService.updatePassword(passwordRequest, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body("비밀번호 변경 성공");
    }
    //좋아요 한 코스 숙소 식당 목록
    @GetMapping("/like")
    public ResponseEntity<?> getMyLike(@AuthenticationPrincipal UserDetails userDetails){
       LikeAttractionDto likeAttractionDto = myPageService.getMyLike(userDetails.getUsername());
        return ResponseEntity.ok(likeAttractionDto);
    }


}
