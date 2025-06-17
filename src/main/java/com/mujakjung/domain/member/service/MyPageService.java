package com.mujakjung.domain.member.service;

import com.mujakjung.domain.member.Member;
import com.mujakjung.domain.member.MemberMapper;
import com.mujakjung.domain.member.MemberRepository;
import com.mujakjung.domain.member.dto.MypageDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyPageService {
    private final MemberRepository memberRepository;
    private final MemberMapper mapper;
    public MypageDto getMyInfo(String username){
        Member member = memberRepository.findByUsername(username).orElseThrow(()-> new IllegalArgumentException("없는 유저 입니다."));
        MypageDto mypageDto = mapper.entityToDto(member);
    }
}
