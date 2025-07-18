package com.mujakjung.domain.member.controller;

import com.mujakjung.domain.member.dto.JoinRequest;
import com.mujakjung.domain.member.service.MemberService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/join")
    public ResponseEntity<?> joinProc(@RequestBody JoinRequest joinRequest) throws IllegalAccessException {
        memberService.join(joinRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("회원정보 저장 성공");
    }
}
