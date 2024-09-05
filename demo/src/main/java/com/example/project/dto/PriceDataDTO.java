package com.example.project.dto;

public class PriceDataDTO {
    private String yyyymm;         // YYYYMM
    private Float priceMJ;         // PriceMJ
    private Float priceMJHeat;     // PriceMJHeat

    // 생성자
    public PriceDataDTO(String yyyymm, Float priceMJ, Float priceMJHeat) {
        this.yyyymm = yyyymm;
        this.priceMJ = priceMJ;
        this.priceMJHeat = priceMJHeat;
    }

    // getter, setter
    public String getYyyymm() {
        return yyyymm;
    }

    public void setYyyymm(String yyyymm) {
        this.yyyymm = yyyymm;
    }

    public Float getPriceMJ() {
        return priceMJ;
    }

    public void setPriceMJ(Float priceMJ) {
        this.priceMJ = priceMJ;
    }

    public Float getPriceMJHeat() {
        return priceMJHeat;
    }

    public void setPriceMJHeat(Float priceMJHeat) {
        this.priceMJHeat = priceMJHeat;
    }
}
