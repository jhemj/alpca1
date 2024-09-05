package com.example.project.repository;

import com.example.project.entity.LNGds;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LNGdsRepository extends JpaRepository<LNGds, String> {
    // PriceMJ_heat 필드가 null인 항목을 찾는 쿼리 메서드
    List<LNGds> findByPriceMJHeatIsNull();  // 필드명과 메서드명이 일치해야 함
}
