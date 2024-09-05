
package com.example.project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "LNGfm")
public class LNGfm {

    @Id
    private String yyyymm;  // YYYYMM 컬럼
    private Float price;    // Price 컬럼
}