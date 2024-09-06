package com.example.project.repository;

import com.example.project.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    // 전화번호와 YYYYMM으로 멤버 조회
    Optional<Member> findByPhoneNumberAndYyyymm(String phoneNumber, String yyyymm);

    // 전화번호로 모든 멤버 조회
    List<Member> findByPhoneNumber(String phoneNumber);

    // 전화번호와 YYYYMM으로 멤버 삭제
    void deleteByPhoneNumberAndYyyymm(String phoneNumber, String yyyymm);
}