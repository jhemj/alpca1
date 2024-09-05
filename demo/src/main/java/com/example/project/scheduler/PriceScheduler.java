package com.example.project.scheduler;

import com.example.project.service.PriceInterpolationService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PriceScheduler {

    private final PriceInterpolationService priceInterpolationService;

    // 생성자 주입 방식 사용
    public PriceScheduler(PriceInterpolationService priceInterpolationService) {
        this.priceInterpolationService = priceInterpolationService;
    }

    // 매달 5일 자정(00:00)에 실행되도록 설정 (cron 표현식 사용)
    @Scheduled(cron = "0 0 0 5 * ?")
    public void updatePricesAutomatically() {
        try {
            // 모든 가격에 대한 보간 작업 수행
            priceInterpolationService.interpolateAll();
        } catch (Exception e) {
            // 예외 처리 로직 추가 (로그 출력, 재시도 등)
            System.err.println("Error during price interpolation: " + e.getMessage());
        }
    }
}
