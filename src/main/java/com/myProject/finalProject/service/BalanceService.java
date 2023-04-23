package com.myProject.finalProject.service;

import com.myProject.finalProject.entity.Balance;
import com.myProject.finalProject.exception.NotEnoughMoneyException;
import com.myProject.finalProject.exception.NotFoundException;
import com.myProject.finalProject.repository.BalanceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class BalanceService {

    private final BalanceRepository repository;

    public Balance putMoney(Long balanceId, BigDecimal income) throws NotFoundException {
        return repository.findById(balanceId)
                .map(balance -> {
                    balance.setBalance(balance.getBalance().add(income));
                    return repository.save(balance);
                })
                .orElseThrow(() ->
                        new NotFoundException("Balance with id " + balanceId + " doesn't exist"));
    }

    public Balance getBalance(Long id) throws NotFoundException {
        Optional<Balance> optionalBalance = repository.findById(id);
        if(optionalBalance.isEmpty()) {
            throw new NotFoundException("Balance with id " + id + " doesn't exist");
        }
        return optionalBalance.get();
    }

    public Balance takeMoney(Long balanceId, BigDecimal spend) throws NotFoundException, NotEnoughMoneyException {
        Balance balance = repository.findById(balanceId)
                .orElseThrow(() -> new NotFoundException("Balance with id " + balanceId + " doesn't exist"));
        BigDecimal result = balance.getBalance().subtract(spend);

        if(result.compareTo(BigDecimal.ZERO) < 0) {
            throw new NotEnoughMoneyException("Balance with id " + balanceId + " has not enough money");
        }

        balance.setBalance(result);
        return repository.save(balance);
    }

    public List<Balance> getAllBalances() {
        return repository.findAll();
    }
}
