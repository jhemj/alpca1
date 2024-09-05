package com.example.project.repository;

import com.example.project.dto.PriceDataDTO;
import com.example.project.entity.LPGhd;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LPGhdRepository extends JpaRepository<LPGhd, String> {

    // PriceMJ가 null인 항목을 찾는 쿼리 메서드
    List<LPGhd> findByPriceMJIsNull();

    // PriceKG가 null인 항목을 찾는 쿼리 메서드
    List<LPGhd> findByPriceKGIsNull();

    // Price 데이터를 반환하는 메서드 (DTO로 매핑)
    @Query("SELECT new com.example.project.dto.PriceDataDTO(lpg.yyyymm, lpg.priceMJ, lng.priceMJHeat) " +
            "FROM LPGhd lpg " +
            "LEFT JOIN LNGds lng ON lpg.yyyymm = lng.yyyymm")
    List<PriceDataDTO> findPriceData();
}
