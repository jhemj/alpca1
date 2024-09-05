package com.example.project.service;

import com.example.project.entity.LPGhd;
import com.example.project.entity.LPGop;
import com.example.project.entity.LNGds;
import com.example.project.entity.LNGfm;
import com.example.project.repository.LPGhdRepository;
import com.example.project.repository.LPGopRepository;
import com.example.project.repository.LNGdsRepository;
import com.example.project.repository.LNGfmRepository;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class PriceInterpolationService {

    private final LPGhdRepository lpgHdRepository;
    private final LPGopRepository lpgOpRepository;
    private final LNGfmRepository lngfmRepository;
    private final LNGdsRepository lngdsRepository;

    @Autowired
    public PriceInterpolationService(LPGhdRepository lpgHdRepository,
                                     LPGopRepository lpgOpRepository,
                                     LNGfmRepository lngfmRepository,
                                     LNGdsRepository lngdsRepository) {
        this.lpgHdRepository = lpgHdRepository;
        this.lpgOpRepository = lpgOpRepository;
        this.lngfmRepository = lngfmRepository;
        this.lngdsRepository = lngdsRepository;
    }

    // LPGhd 결측치 보간
    @Transactional
    public void interpolateLPGhdPrices() {
        List<LPGhd> lpgHdList = lpgHdRepository.findByPriceMJIsNull();
        SimpleRegression regression = createLPGRegressionModel();

        for (LPGhd lpgHd : lpgHdList) {
            if (lpgHd.getPriceKG() != null) {
                double interpolatedPriceMJ = regression.predict(lpgHd.getPriceKG());
                lpgHd.setPriceMJ((float) interpolatedPriceMJ);
                lpgHd.setActual(false); // 보간된 값
            }
        }

        lpgHdRepository.saveAll(lpgHdList); // 배치 업데이트
    }

    // LNGds 결측치 보간
    @Transactional
    public void interpolateLNGdsPrices() {
        List<LNGds> lngdsList = lngdsRepository.findByPriceMJHeatIsNull(); // 필드명이 정확한지 확인 필요
        SimpleRegression regression = createLNGRegressionModel();

        for (LNGds lngds : lngdsList) {
            if (lngds.getPriceMJIndMax() != null) { // Max 값을 기준으로 회귀 예측
                double interpolatedPriceMJ = regression.predict(lngds.getPriceMJIndMax());
                lngds.setPriceMJHeat((float) interpolatedPriceMJ); // 필드명이 정확한지 확인 필요
                lngds.setActual(false); // 보간된 값
            }
        }

        lngdsRepository.saveAll(lngdsList); // 배치 업데이트
    }

    // 두 가지 보간 작업을 모두 실행하는 메서드
    @Transactional
    public void interpolateAll() {
        interpolateLPGhdPrices();
        interpolateLNGdsPrices();
    }

    // LPGhd에 대한 회귀 분석 모델 생성
    private SimpleRegression createLPGRegressionModel() {
        SimpleRegression regression = new SimpleRegression();
        List<LPGop> lpgOpList = lpgOpRepository.findAll(); // 참고 데이터를 가져옴

        for (LPGop lpgOp : lpgOpList) {
            if (lpgOp.getPriceMJ() != null && lpgOp.getPriceKG() != null) {
                regression.addData(lpgOp.getPriceKG(), lpgOp.getPriceMJ());
            }
        }

        return regression;
    }

    // LNGds에 대한 회귀 분석 모델 생성
    private SimpleRegression createLNGRegressionModel() {
        SimpleRegression regression = new SimpleRegression();
        List<LNGfm> lngfmList = lngfmRepository.findAll(); // LNG 데이터 가져옴

        for (LNGfm lngfm : lngfmList) {
            if (lngfm.getPrice() != null) {
                // LNG 가격 데이터로 회귀 분석 모델 구성
                regression.addData(lngfm.getPrice(), lngfm.getPrice());
            }
        }

        return regression;
    }
}
