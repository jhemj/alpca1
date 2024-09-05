package com.example.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ExpDataDTO {
    private String yyyymm;              // YYYYMM
    private Float lpgPriceMJ;           // LPGhd.PriceMJ
    private Float lpgPriceKG;           // LPGhd.PriceKG
    private Float lngPriceMJ_heat;      // LNGds.PriceMJ_heat
    private Float lngPriceMJ_Ind_Max;   // LNGds.PriceMJ_Ind_Max
    private Float lngPriceMJ_Ind_Min;   // LNGds.PriceMJ_Ind_Min
    private Float charge;               // members.charge
    private Float lpgfmPrice;           // LPGfm.Price
    private Float lngfmPrice;           // LNGfm.Price

    private Float exp_MJ_heat;          // 계산된 값 1
    private Float exp_MJ_Ind_Max;       // 계산된 값 2
    private Float exp_MJ_Ind_Min;       // 계산된 값 3
}
