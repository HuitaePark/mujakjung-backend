package com.mujakjung.domain.member;


import com.mujakjung.domain.member.dto.JoinRequest;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberMapper memberMapper;
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    public void join(JoinRequest joinRequest) throws IllegalAccessException {

        if(memberRepository.existsByMemberId(joinRequest.getMemberId())){
            throw new IllegalAccessException("존재하는 닉네임입니다.");
        }

        Member member = memberMapper.requestToMember(joinRequest);
        member.updateEncryptionPassword(bCryptPasswordEncoder);
        memberRepository.save(member);
    }

}
