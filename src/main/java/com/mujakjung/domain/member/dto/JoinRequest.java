package com.mujakjung.domain.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class JoinRequest {
    private String username;
    private String password;
    private String nickname;
}
