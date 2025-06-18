package com.mujakjung.domain.member.service;

import com.mujakjung.domain.member.Member;
import com.mujakjung.domain.member.MemberMapper;
import com.mujakjung.domain.member.MemberRepository;
import com.mujakjung.domain.member.dto.MypageDto;
import com.mujakjung.domain.member.dto.PasswordRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MyPageService {
    private final MemberRepository memberRepository;
    private final MemberMapper mapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public MypageDto getMyInfo(String username){
        Member member = memberRepository.findByUsername(username).orElseThrow(()-> new IllegalArgumentException("없는 유저 입니다."));
        System.out.println(member);
        return mapper.entityToDto(member);
    }

    public void updatePassword(PasswordRequest passwordRequest, String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(()->new IllegalArgumentException("유저를 찾을수 없습니다."));
        if(member.checkPassword(passwordRequest.getCurrentPassword(), bCryptPasswordEncoder)){
           member.updatePassword(passwordRequest.getUpdatePassword(), bCryptPasswordEncoder);
           memberRepository.save(member);
        }
        else{
            throw new IllegalArgumentException("유저를 찾을수 없습니다.");
        }
    }
}
