package com.example.project.service;

import com.example.project.dto.ExpDataDTO;
import com.example.project.repository.ExpDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpDataService {

    private final ExpDataRepository expDataRepository;

    @Autowired
    public ExpDataService(ExpDataRepository expDataRepository) {
        this.expDataRepository = expDataRepository;
    }



    // 전화번호로 데이터를 가져오는 메서드
    public List<ExpDataDTO> getExpDataByPhoneNumber(String phoneNumber) {
        return expDataRepository.findExpDataByPhoneNumber(phoneNumber);
    }
}
