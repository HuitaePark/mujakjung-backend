package com.mujakjung.domain.member.service;

import com.mujakjung.domain.member.CustomUserDetails;
import com.mujakjung.domain.member.Member;
import com.mujakjung.domain.member.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CustomUserDetailService implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByUsername(username).orElseThrow(()->new IllegalArgumentException("없는 유저 입니다."));

        if(member!= null){
            return new CustomUserDetails(member);
        }

        return null;
    }
}
