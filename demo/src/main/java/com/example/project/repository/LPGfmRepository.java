package com.example.project.repository;

import com.example.project.entity.LPGfm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LPGfmRepository extends JpaRepository<LPGfm, String> {
}
