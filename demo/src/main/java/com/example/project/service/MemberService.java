package com.example.project.service;

import com.example.project.entity.Member;
import com.example.project.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    // 전화번호와 YYYYMM으로 멤버 조회
    public Optional<Member> getMemberById(String phoneNumber, String yyyymm) {
        return memberRepository.findByPhoneNumberAndYyyymm(phoneNumber, yyyymm);
    }

    // **멤버 생성 또는 업데이트**
    public Member createOrUpdateMember(Member member) {
        // phoneNumber와 yyyymm으로 기존 데이터를 조회
        Optional<Member> existingMember = memberRepository.findByPhoneNumberAndYyyymm(member.getPhoneNumber(), member.getYyyymm());

        // 기존 멤버가 있으면 업데이트, 없으면 새로 생성
        if (existingMember.isPresent()) {
            Member updateMember = existingMember.get();
            updateMember.setCharge(member.getCharge());
            updateMember.setActual(member.getActual());
            // 필요한 다른 필드도 업데이트
            return memberRepository.save(updateMember); // 갱신
        } else {
            return memberRepository.save(member); // 새로 생성
        }
    }

    // 전화번호로 모든 멤버 조회
    public List<Member> getMembersByPhoneNumber(String phoneNumber) {
        return memberRepository.findByPhoneNumber(phoneNumber);
    }

    // 멤버 삭제
    public void deleteMember(String phoneNumber, String yyyymm) {
        memberRepository.deleteByPhoneNumberAndYyyymm(phoneNumber, yyyymm);
    }
}
