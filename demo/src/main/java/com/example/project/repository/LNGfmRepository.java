package com.example.project.repository;

import com.example.project.entity.LNGfm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LNGfmRepository extends JpaRepository<LNGfm, String> {
}
