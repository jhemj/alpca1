package com.example.project.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "LPGop")
public class LPGop {

    @Id
    private String yyyymm;      // YYYYMM 컬럼, Primary Key
    private Float priceMJ;      // PriceMJ 컬럼
    private Float priceKG;      // PriceKG 컬럼 (보간에 사용)
    private Boolean actual;     // Actual 컬럼
}