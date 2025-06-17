package com.mujakjung.domain.member;

import com.mujakjung.domain.member.dto.JoinRequest;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;

}
