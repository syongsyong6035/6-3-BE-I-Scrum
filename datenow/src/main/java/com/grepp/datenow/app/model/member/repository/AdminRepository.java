package com.grepp.datenow.app.model.member.repository;

import com.grepp.datenow.app.model.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Member,Integer> {

}
