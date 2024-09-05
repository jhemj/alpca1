package com.example.project.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "members")
public class Member {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID uuid;         // UUID 컬럼, Primary Key, 자동 생성

    @Column(nullable = false, length = 15)
    private String phoneNumber; // 전화번호 컬럼, NOT NULL

    @Column(nullable = false, length = 6)
    private String yyyymm;     // YYYYMM 컬럼, NOT NULL

    @Column(nullable = false)
    private Boolean actual = true;    // Actual 컬럼, NOT NULL

    @Column(nullable = false)
    private Float charge;      // charge 컬럼, NOT NULL
}
