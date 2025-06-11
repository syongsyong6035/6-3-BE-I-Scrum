package com.grepp.datenow.app.model.member.repository;

import com.grepp.datenow.app.model.member.entity.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer> {
    boolean existsByUserId(String userId);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    Optional<Member> findByUserId(String userId);
    Optional<Member> findByEmail(String email);
}
