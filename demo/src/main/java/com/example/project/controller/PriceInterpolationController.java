// PriceInterpolationController.java
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

    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/lpg")
    public ResponseEntity<String> interpolateLPGPrices() {
        interpolationService.interpolateLPGhdPrices();
        return ResponseEntity.ok("LPGhd 결측치 보간 완료");
    }
    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping("/lng")
    public ResponseEntity<String> interpolateLNGPrices() {
        interpolationService.interpolateLNGdsPrices();
        return ResponseEntity.ok("LNGds 결측치 보간 완료");
    }
}
