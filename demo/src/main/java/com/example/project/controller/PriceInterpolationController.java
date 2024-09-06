package com.example.project.controller;

import com.example.project.service.PriceInterpolationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/interpolation")
public class PriceInterpolationController {

    @Autowired
    private PriceInterpolationService interpolationService;

    // LPG 보간 작업
    @GetMapping("/lpg")
    public ResponseEntity<String> interpolateLPGPrices() {
        interpolationService.interpolateLPG();  // 통합된 LPG 보간 작업 메서드 호출
        return ResponseEntity.ok("LPGhd 결측치 보간 완료");
    }

    // LNG 보간 작업
    @GetMapping("/lng")
    public ResponseEntity<String> interpolateLNGPrices() {
        interpolationService.interpolateLNG();  // 통합된 LNG 보간 작업 메서드 호출
        return ResponseEntity.ok("LNGds 결측치 보간 완료");
    }

    // 전체 보간 작업 (LPG와 LNG 모두)
    @GetMapping("/all")
    public ResponseEntity<String> interpolateAllPrices() {
        interpolationService.interpolateAll();  // LPG와 LNG 모두 보간하는 메서드 호출
        return ResponseEntity.ok("LPG 및 LNG 결측치 보간 완료");
    }
}
