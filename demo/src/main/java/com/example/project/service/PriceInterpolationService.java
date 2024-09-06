package com.example.project.service;

import com.example.project.entity.*;
import com.example.project.repository.*;
import org.apache.commons.math3.stat.regression.SimpleRegression;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PriceInterpolationService {

    private final LPGhdRepository lpgHdRepository;
    private final LPGopRepository lpgOpRepository;
    private final LPGfmRepository lpgfmRepository;
    private final LNGfmRepository lngfmRepository;
    private final LNGdsRepository lngdsRepository;

    @Autowired
    public PriceInterpolationService(LPGhdRepository lpgHdRepository,
                                     LPGopRepository lpgOpRepository,
                                     LPGfmRepository lpgfmRepository,
                                     LNGfmRepository lngfmRepository,
                                     LNGdsRepository lngdsRepository) {
        this.lpgHdRepository = lpgHdRepository;
        this.lpgOpRepository = lpgOpRepository;
        this.lpgfmRepository = lpgfmRepository;
        this.lngfmRepository = lngfmRepository;
        this.lngdsRepository = lngdsRepository;
    }

    @Transactional
    public void interpolateAll() {
        // 1. 먼저 미래 시점의 더미 데이터를 추가
        addFutureDummyDataForLPGhd();
        addFutureDummyDataForLNGds();

        // 2. 그 후 기존의 보간 작업을 수행
        interpolateLPG();
        interpolateLNG();
    }

    // LPGhd의 미래 데이터를 추가하는 메서드 (현시점 기준 +2개월)
    private void addFutureDummyDataForLPGhd() {
        String twoMonthsLater = getFutureYYYYMM(4);  // 현시점 기준 +2개월
        if (!lpgHdRepository.existsById(twoMonthsLater)) {
            LPGhd futureLpgHd = new LPGhd();
            futureLpgHd.setYyyymm(twoMonthsLater);
            futureLpgHd.setPriceMJ(0f);   // 0으로 초기화
            futureLpgHd.setPriceKG(0f);   // 0으로 초기화
            futureLpgHd.setActual(false); // 보간된 값으로 설정
            lpgHdRepository.save(futureLpgHd);
        }
    }

    // LNGds의 미래 데이터를 추가하는 메서드 (현시점 기준 +4개월)
    private void addFutureDummyDataForLNGds() {
        String fourMonthsLater = getFutureYYYYMM(4);  // 현시점 기준 +4개월
        if (!lngdsRepository.existsById(fourMonthsLater)) {
            LNGds futureLngds = new LNGds();
            futureLngds.setYyyymm(fourMonthsLater);
            futureLngds.setPriceMJHeat(0f);       // 0으로 초기화
            futureLngds.setPriceMJIndMin(0f);     // 0으로 초기화
            futureLngds.setPriceMJIndMax(0f);     // 0으로 초기화
            futureLngds.setActual(false);         // 보간된 값으로 설정
            lngdsRepository.save(futureLngds);
        }
    }

    // YYYYMM 형식으로 현재 날짜 기준 특정 개월 후의 날짜를 반환하는 메서드
    private String getFutureYYYYMM(int monthsToAdd) {
        LocalDate futureDate = LocalDate.now().plusMonths(monthsToAdd);
        return futureDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
    }

    // <LPG> 보간 작업
    @Transactional
    public void interpolateLPG() {
        // 1. LPGop 기반으로 LPGhd의 PriceMJ 보간
        interpolateLPGhdUsingLPGop();

        // 2. LPGfm과 LPGhd의 상관관계 분석을 통한 보간
        interpolateLPGhdUsingLPGfm();
    }

    // <LNG> 보간 작업
    @Transactional
    public void interpolateLNG() {
        // 1. LNGfm과 LNGds의 상관관계 분석을 통한 보간
        interpolateLNGdsUsingLNGfm();

        // 2. LNGds의 priceMJHeat, priceMJIndMin, priceMJIndMax 간의 상관관계 분석
        interpolateLNGdsInternal();
    }

    // 1. LPGop와 LPGhd의 회귀분석을 이용한 PriceMJ 보간
    private void interpolateLPGhdUsingLPGop() {
        List<LPGhd> lpgHdList = lpgHdRepository.findByPriceMJIsNull();
        SimpleRegression regression = createLPGRegressionModel();

        for (LPGhd lpgHd : lpgHdList) {
            if (lpgHd.getPriceKG() != null) {
                double interpolatedPriceMJ = regression.predict(lpgHd.getPriceKG());
                lpgHd.setPriceMJ((float) interpolatedPriceMJ);
                lpgHd.setActual(false); // 보간된 값
            }
        }

        lpgHdRepository.saveAll(lpgHdList);
    }

    // 2. LPGfm과 LPGhd 간의 상관관계 분석을 통한 보간 (2달 후의 데이터 매핑)
    private void interpolateLPGhdUsingLPGfm() {
        List<LPGfm> lpgfmList = lpgfmRepository.findAll();
        List<LPGhd> lpgHdList = lpgHdRepository.findAll();

        Map<String, LPGhd> lpgHdMap = lpgHdList.stream()
            .collect(Collectors.toMap(LPGhd::getYyyymm, lpgHd -> lpgHd));

        SimpleRegression regression = new SimpleRegression();

        // 회귀 모델 구성
        for (LPGfm lpgfm : lpgfmList) {
            // Null 값 체크
            if (lpgfm.getPrice() == null) {
                continue; // price가 null인 데이터를 건너뜁니다.
            }

            String mappedYyyymm = incrementMonth(lpgfm.getYyyymm(), 2); // 2달 후 데이터 매핑
            LPGhd correspondingLpgHd = lpgHdMap.get(mappedYyyymm);
            if (correspondingLpgHd != null && correspondingLpgHd.getPriceMJ() != null) {
                regression.addData(lpgfm.getPrice(), correspondingLpgHd.getPriceMJ());
            }
        }

        // 결측치 보간
        for (LPGhd lpgHd : lpgHdList) {
            if (lpgHd.getPriceMJ() == null) {
                String mappedYyyymm = decrementMonth(lpgHd.getYyyymm(), 2); // 2달 전의 LPGfm과 매핑
                LPGfm correspondingLpgfm = lpgfmList.stream()
                    .filter(lpgfm -> lpgfm.getYyyymm().equals(mappedYyyymm) && lpgfm.getPrice() != null)
                    .findFirst()
                    .orElse(null);

                if (correspondingLpgfm != null) {
                    double interpolatedPriceMJ = regression.predict(correspondingLpgfm.getPrice());
                    lpgHd.setPriceMJ((float) interpolatedPriceMJ);
                    lpgHd.setActual(false); // 보간된 값
                }
            }
        }

        lpgHdRepository.saveAll(lpgHdList);
    }

    // 1. LNGfm과 LNGds 간의 상관관계 분석을 통한 보간 (4달 후의 데이터 매핑)
    private void interpolateLNGdsUsingLNGfm() {
        List<LNGfm> lngfmList = lngfmRepository.findAll();
        List<LNGds> lngdsList = lngdsRepository.findAll();

        Map<String, LNGds> lngdsMap = lngdsList.stream()
            .collect(Collectors.toMap(LNGds::getYyyymm, lngds -> lngds));

        SimpleRegression regression = new SimpleRegression();

        // 회귀 모델 구성
        for (LNGfm lngfm : lngfmList) {
            String mappedYyyymm = incrementMonth(lngfm.getYyyymm(), 4); // 4달 후의 데이터 매핑
            LNGds correspondingLngds = lngdsMap.get(mappedYyyymm);
            if (correspondingLngds != null && correspondingLngds.getPriceMJHeat() != null) {
                regression.addData(lngfm.getPrice(), correspondingLngds.getPriceMJHeat());
            }
        }

        // 결측치 보간
        for (LNGds lngds : lngdsList) {
            if (lngds.getPriceMJHeat() == null) {
                String mappedYyyymm = decrementMonth(lngds.getYyyymm(), 4); // 4달 전의 LNGfm과 매핑
                LNGfm correspondingLngfm = lngfmList.stream()
                    .filter(lngfm -> lngfm.getYyyymm().equals(mappedYyyymm))
                    .findFirst()
                    .orElse(null);

                if (correspondingLngfm != null) {
                    double interpolatedPriceMJHeat = regression.predict(correspondingLngfm.getPrice());
                    lngds.setPriceMJHeat((float) interpolatedPriceMJHeat);
                    lngds.setActual(false); // 보간된 값
                }
            }
        }

        lngdsRepository.saveAll(lngdsList);
    }

    // 2. LNGds 내부의 priceMJHeat, priceMJIndMin, priceMJIndMax 간의 상관관계 분석 및 보간
    private void interpolateLNGdsInternal() {
        List<LNGds> lngdsList = lngdsRepository.findAll();

        SimpleRegression minRegression = new SimpleRegression();
        SimpleRegression maxRegression = new SimpleRegression();

        // 회귀 모델 구성
        for (LNGds lngds : lngdsList) {
            if (lngds.getPriceMJHeat() != null) {
                if (lngds.getPriceMJIndMin() != null) {
                    minRegression.addData(lngds.getPriceMJHeat(), lngds.getPriceMJIndMin());
                }
                if (lngds.getPriceMJIndMax() != null) {
                    maxRegression.addData(lngds.getPriceMJHeat(), lngds.getPriceMJIndMax());
                }
            }
        }

        // 결측치 보간
        for (LNGds lngds : lngdsList) {
            if (lngds.getPriceMJIndMin() == null && lngds.getPriceMJHeat() != null) {
                double interpolatedPriceMJIndMin = minRegression.predict(lngds.getPriceMJHeat());
                lngds.setPriceMJIndMin((float) interpolatedPriceMJIndMin);
                lngds.setActual(false);
            }
            if (lngds.getPriceMJIndMax() == null && lngds.getPriceMJHeat() != null) {
                double interpolatedPriceMJIndMax = maxRegression.predict(lngds.getPriceMJHeat());
                lngds.setPriceMJIndMax((float) interpolatedPriceMJIndMax);
                lngds.setActual(false);
            }
        }

        lngdsRepository.saveAll(lngdsList);
    }

    // 회귀 분석 모델 생성 (LPGop 기반)
    private SimpleRegression createLPGRegressionModel() {
        SimpleRegression regression = new SimpleRegression();
        List<LPGop> lpgOpList = lpgOpRepository.findAll();

        for (LPGop lpgOp : lpgOpList) {
            if (lpgOp.getPriceMJ() != null && lpgOp.getPriceKG() != null) {
                regression.addData(lpgOp.getPriceKG(), lpgOp.getPriceMJ());
            }
        }

        return regression;
    }

    // YYYYMM 값을 달 단위로 증가시키는 함수
    private String incrementMonth(String yyyymm, int months) {
        int year = Integer.parseInt(yyyymm.substring(0, 4));
        int month = Integer.parseInt(yyyymm.substring(4, 6));
        month += months;

        while (month > 12) {
            month -= 12;
            year += 1;
        }

        return String.format("%04d%02d", year, month);
    }

    // YYYYMM 값을 달 단위로 감소시키는 함수
    private String decrementMonth(String yyyymm, int months) {
        int year = Integer.parseInt(yyyymm.substring(0, 4));
        int month = Integer.parseInt(yyyymm.substring(4, 6));
        month -= months;

        while (month < 1) {
            month += 12;
            year -= 1;
        }

        return String.format("%04d%02d", year, month);
    }
}
