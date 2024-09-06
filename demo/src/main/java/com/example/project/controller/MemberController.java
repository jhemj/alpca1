package com.example.project.controller;

import com.example.project.entity.Member;
import com.example.project.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/members")
@CrossOrigin(origins = "http://localhost:3000")
public class MemberController {

    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    // **멤버 생성 또는 업데이트**
    @PostMapping
    public ResponseEntity<Member> createOrUpdateMember(@RequestBody Member member) {
        Member createdMember = memberService.createOrUpdateMember(member);
        return ResponseEntity.ok(createdMember);
    }



    // 멤버 조회 (전화번호와 YYYYMM으로 조회)
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{phoneNumber}/{yyyymm}")
    public ResponseEntity<Member> getMember(@PathVariable String phoneNumber, @PathVariable String yyyymm) {
        Optional<Member> member = memberService.getMemberById(phoneNumber, yyyymm);
        return member.map(ResponseEntity::ok)
                     .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // 전화번호로 모든 멤버 조회
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/{phoneNumber}")
    public ResponseEntity<List<Member>> getMembersByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
        List<Member> members = memberService.getMembersByPhoneNumber(phoneNumber);
        if (members.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } else {
            return ResponseEntity.ok(members);
        }
    }

    // 멤버 삭제
    @CrossOrigin(origins = "http://localhost:3000")
    @DeleteMapping("/{phoneNumber}/{yyyymm}")
    public ResponseEntity<Void> deleteMember(@PathVariable String phoneNumber, @PathVariable String yyyymm) {
        memberService.deleteMember(phoneNumber, yyyymm);
        return ResponseEntity.noContent().build();
    }
}