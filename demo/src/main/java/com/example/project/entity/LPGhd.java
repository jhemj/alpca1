package com.example.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "LPGhd")
public class LPGhd {

    @Id
    private String yyyymm;       // YYYYMM 컬럼, Primary Key
    private Float priceMJ;       // PriceMJ 컬럼 (MJ 단위 가격)
    private Float priceKG;       // PriceKG 컬럼 (KG 단위 가격)
    private Boolean actual;      // Actual 컬럼 (실제 여부)
}