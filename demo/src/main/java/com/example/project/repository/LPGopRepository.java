package com.example.project.repository;

import com.example.project.entity.LPGop;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LPGopRepository extends JpaRepository<LPGop, String> {
    // 기본 CRUD 및 findById 메서드를 통해 YYYYMM 기준으로 LPGop 데이터 조회 가능
}
