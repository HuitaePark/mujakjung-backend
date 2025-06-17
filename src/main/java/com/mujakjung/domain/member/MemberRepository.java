package com.mujakjung.domain.member;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {
    boolean existsByMemberId(String memberId);
    Optional<Member> findByMemberId(String memberId);
}
