package com.mujakjung.domain.member.service;


import com.mujakjung.domain.member.Member;
import com.mujakjung.domain.member.MemberMapper;
import com.mujakjung.domain.member.MemberRepository;
import com.mujakjung.domain.member.dto.JoinRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public void join(JoinRequest joinRequest) throws IllegalAccessException {

        if(memberRepository.existsByUsername(joinRequest.getUsername())){
            throw new IllegalAccessException("존재하는 아이디입니다.");
        }

        Member member = memberMapper.requestToMember(joinRequest);
        member.encryptionPassword(bCryptPasswordEncoder);
        memberRepository.save(member);
    }

}
