package com.example.project.entity;


import jakarta.persistence.Id;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "LNGds")
@Getter
@Setter
@NoArgsConstructor
public class LNGds {

    @Id
    @Column(name = "YYYYMM", length = 6)
    private String yyyymm;

    @Column(name = "PriceMJ_heat")
    private Float priceMJHeat;

    @Column(name = "PriceMJ_Ind_Max")
    private Float priceMJIndMax;

    @Column(name = "priceMJ_Ind_Min")
    private Float priceMJIndMin;

    @Column(name = "Actual")
    private Boolean actual;

    @ManyToOne
    @JoinColumn(name = "YYYYMM", referencedColumnName = "YYYYMM", foreignKey = @ForeignKey(name = "fk_lpghd_yyyymm"), insertable = false, updatable = false)
    private LPGhd lpgHd; // 외래 키 관계를 표현하는 객체
}
