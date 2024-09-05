package com.example.project.controller;

import com.example.project.dto.PriceDataDTO;
import com.example.project.service.PriceInterpolationService;
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
@Tag(name = "Price Data", description = "LPGhd와 LNGds의 가격 데이터와 보간 작업에 대한 API")
public class PriceController {

    private final PriceService priceService;
    private final PriceInterpolationService priceInterpolationService;

    @Autowired
    public PriceController(PriceService priceService, PriceInterpolationService priceInterpolationService) {
        this.priceService = priceService;
        this.priceInterpolationService = priceInterpolationService;
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/price/interpolate")
    @Operation(summary = "PriceMJ 및 PriceKG 보간 작업", description = "LPGhd 및 LNGds의 결측치에 대해 PriceMJ와 PriceKG 보간 작업을 실행합니다.")
    public ResponseEntity<String> interpolateAllPrices() {
        priceInterpolationService.interpolateAll();
        return ResponseEntity.ok("PriceMJ 및 PriceKG 보간 작업이 완료되었습니다.");
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/price")
    @Operation(summary = "LPG와 LNG 가격 데이터를 조회", description = "YYYYMM 기준으로 LPG와 LNG 가격 데이터를 조회합니다.")
    public ResponseEntity<List<PriceDataDTO>> getPriceData() {
        List<PriceDataDTO> priceDataList = priceService.getPriceData();
        return ResponseEntity.ok(priceDataList);
    }
}
