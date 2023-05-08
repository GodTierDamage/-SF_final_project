package com.myProject.finalProject.service;

import com.myProject.finalProject.entity.Balance;
import com.myProject.finalProject.entity.Income;
import com.myProject.finalProject.entity.Spend;
import com.myProject.finalProject.entity.Transaction;
import com.myProject.finalProject.enums.OperationType;
import com.myProject.finalProject.enums.SpendingType;
import com.myProject.finalProject.exception.BalanceNotFoundException;
import com.myProject.finalProject.exception.CreateBalanceException;
import com.myProject.finalProject.exception.NotEnoughMoneyException;
import com.myProject.finalProject.repository.BalanceRepository;
import com.myProject.finalProject.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Service
@AllArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final TransactionRepository transactionsRepository;

    @Transactional
    public Balance putMoney(Transaction income) throws BalanceNotFoundException {
        Balance balance = balanceRepository
                .findById(income.balanceId())
                .orElseThrow(() -> new BalanceNotFoundException(income.balanceId()));
        balance.amount(balance.amount().add(income.amount()));

        transactionsRepository.save(income);
        return balanceRepository.save(balance);
    }

    public Balance getBalance(Long id) throws BalanceNotFoundException {
        return balanceRepository.findById(id).orElseThrow(() -> new BalanceNotFoundException(id));
    }

    @Transactional
    public Balance takeMoney(Transaction spend) throws BalanceNotFoundException, NotEnoughMoneyException {
        Balance balance = balanceRepository.findById(spend.balanceId())
                .orElseThrow(() -> new BalanceNotFoundException(spend.balanceId()));
        BigDecimal result = balance.amount().subtract(spend.amount());

        if(result.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughMoneyException("Balance with id " + spend.balanceId() + " has not enough money");
        }

        balance.amount(result);

        transactionsRepository.save(spend);
        return balanceRepository.save(balance);
    }

    @Transactional
    public boolean transferMoney(Transaction transaction) throws BalanceNotFoundException, NotEnoughMoneyException {
        if(!transaction.operationType().equals(OperationType.SPEND)) {
            throw new IllegalArgumentException("Illegal type of operationType for transfer");
        }

        Spend transfer = (Spend) transaction;
        Income income = Income.builder()
                .balanceId(transfer.receiverId())
                .amount(transfer.amount())
                .message("Transfer from " + transfer.balanceId())
                .build();

        Balance receiver = balanceRepository.findById(transfer.receiverId())
                .orElseThrow(() -> new BalanceNotFoundException(transfer.receiverId()));

        transfer.typeOfSpendingOperation(SpendingType.TRANSFER);
        takeMoney(transfer);
        putMoney(income);

        return true;
    }

    public Balance addBalance(Balance balance) throws CreateBalanceException {
        if(balanceRepository.findById(balance.id()).isPresent()) {
            throw new CreateBalanceException("Balance with id " + balance.id() + " already exists");
        }
        balanceRepository.save(balance);
        return balance;
    }

    public List<Transaction> findAllTransactionsByBalanceId(Long balanceId) {
        return transactionsRepository.findByBalanceId(balanceId);
    }

    public List<Transaction> findByDateBefore(Long id, Timestamp date) {
        return transactionsRepository.findByBalanceIdAndDateOfOperationBefore(id, date);
    }

    public List<Transaction> findByDateBetween(Long balanceId, Timestamp startDate, Timestamp endDate) {
        return transactionsRepository.findByBalanceIdAndDateOfOperationBetween(balanceId, startDate, endDate);
    }

    public List<Transaction> findByDateAfter(Long balanceId, Timestamp date) {
        return transactionsRepository.findByBalanceIdAndDateOfOperationAfter(balanceId, date);
    }


}
