package com.myProject.finalProject.service;

import com.myProject.finalProject.entity.Balance;
import com.myProject.finalProject.entity.Income;
import com.myProject.finalProject.entity.Spend;
import com.myProject.finalProject.exception.NotEnoughMoneyException;
import com.myProject.finalProject.exception.BalanceNotFoundException;
import com.myProject.finalProject.repository.BalanceRepository;
import com.myProject.finalProject.repository.IncomeRepository;
import com.myProject.finalProject.repository.SpendRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@AllArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final IncomeRepository incomeRepository;
    private final SpendRepository spendRepository;

    public Balance putMoney(Income income) throws BalanceNotFoundException {
        Balance balance = balanceRepository
                .findById(income.getBalanceId())
                .orElseThrow(() -> new BalanceNotFoundException(income.getBalanceId()));
        balance.setBalance(balance.getBalance().add(income.getIncome()));

        incomeRepository.save(income);
        return balanceRepository.save(balance);
    }

    public Balance getBalance(Long id) throws BalanceNotFoundException {
        return balanceRepository.findById(id).orElseThrow(() -> new BalanceNotFoundException(id));
    }

    public Balance takeMoney(Spend spend) throws BalanceNotFoundException, NotEnoughMoneyException {
        Balance balance = balanceRepository.findById(spend.getBalanceId())
                .orElseThrow(() -> new BalanceNotFoundException(spend.getBalanceId()));
        BigDecimal result = balance.getBalance().subtract(spend.getSpend());

        if(result.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughMoneyException("Balance with id " + spend.getBalanceId() + " has not enough money");
        }

        balance.setBalance(result);

        spendRepository.save(spend);
        return balanceRepository.save(balance);
    }


}
