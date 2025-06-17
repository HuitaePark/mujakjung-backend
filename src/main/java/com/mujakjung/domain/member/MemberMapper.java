package com.mujakjung.domain.member;

import com.mujakjung.domain.member.dto.JoinRequest;
import com.mujakjung.domain.member.dto.MypageDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface MemberMapper {

    @Mapping(target = "role",constant = "USER")
    Member requestToMember(JoinRequest joinRequest);

    MypageDto entityToDto(Member member);
}
