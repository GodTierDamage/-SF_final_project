package com.myProject.finalProject.repository;

import com.myProject.finalProject.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByBalanceIdAndDateOfOperationBefore(Long balanceId, Timestamp endDate);

    List<Transaction> findByBalanceIdAndDateOfOperationBetween(Long balanceId, Timestamp startDate, Timestamp endDate);

    List<Transaction> findByBalanceIdAndDateOfOperationAfter(Long balanceId, Timestamp endDate);

    List<Transaction> findByBalanceId(Long balanceId);
}
