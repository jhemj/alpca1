package com.example.project.controller;

import com.example.project.dto.PriceDataDTO;
import com.example.project.service.PriceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "Price Data", description = "LPG와 LNG 가격 데이터를 조회하는 API")
@CrossOrigin(origins = "http://localhost:3000")
public class PriceController {

    private final PriceService priceService;

    @Autowired
    public PriceController(PriceService priceService) {
        this.priceService = priceService;
    }

    @GetMapping("/price")
    @Operation(summary = "LPG와 LNG 가격 데이터를 조회", description = "YYYYMM 기준으로 LPG와 LNG 가격 데이터를 조회합니다.")
    public ResponseEntity<List<PriceDataDTO>> getPriceData() {
        List<PriceDataDTO> priceDataList = priceService.getPriceData();
        return ResponseEntity.ok(priceDataList);
    }
}
