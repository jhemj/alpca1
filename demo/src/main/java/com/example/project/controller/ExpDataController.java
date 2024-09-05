package com.example.project.controller;

import com.example.project.dto.ExpDataDTO;
import com.example.project.service.ExpDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/expdata")
@CrossOrigin(origins = "http://localhost:3000")
public class ExpDataController {

    private final ExpDataService expDataService;

    @Autowired
    public ExpDataController(ExpDataService expDataService) {
        this.expDataService = expDataService;
    }

    // 전화번호로 데이터를 조회하는 엔드포인트
    @GetMapping("/{phoneNumber}")
    public ResponseEntity<List<ExpDataDTO>> getExpDataByPhoneNumber(@PathVariable("phoneNumber") String phoneNumber) {
        List<ExpDataDTO> expDataList = expDataService.getExpDataByPhoneNumber(phoneNumber);
        if (expDataList.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(expDataList);
    }
}
