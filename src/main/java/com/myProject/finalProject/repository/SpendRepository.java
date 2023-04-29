package com.myProject.finalProject.repository;

import com.myProject.finalProject.entity.Spend;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpendRepository extends JpaRepository<Spend, Long> {
}
