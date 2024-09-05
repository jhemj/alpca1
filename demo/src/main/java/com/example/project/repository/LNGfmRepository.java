package com.example.project.repository;

import com.example.project.entity.LNGfm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LNGfmRepository extends JpaRepository<LNGfm, String> {
    // 기본 CRUD 작업 제공
}
