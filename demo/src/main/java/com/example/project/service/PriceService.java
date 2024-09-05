// PriceService.java
package com.example.project.service;

import com.example.project.dto.PriceDataDTO;
import com.example.project.repository.LPGhdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PriceService {

    private final LPGhdRepository lpgHdRepository;

    @Autowired
    public PriceService(LPGhdRepository lpgHdRepository) {
        this.lpgHdRepository = lpgHdRepository;
    }

    // LPG와 LNG 데이터를 조회하는 메서드 (PriceDataDTO로 반환)
    public List<PriceDataDTO> getPriceData() {
        return lpgHdRepository.findPriceData();
    }
}
