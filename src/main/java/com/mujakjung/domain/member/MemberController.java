package com.mujakjung.domain.member;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

}
