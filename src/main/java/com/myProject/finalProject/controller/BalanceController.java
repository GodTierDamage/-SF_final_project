package com.myProject.finalProject.controller;

import com.myProject.finalProject.entity.Balance;
import com.myProject.finalProject.entity.Income;
import com.myProject.finalProject.entity.Spend;
import com.myProject.finalProject.exception.NotEnoughMoneyException;
import com.myProject.finalProject.exception.NotFoundException;
import com.myProject.finalProject.service.BalanceService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "api/balance")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/getBalance")
    @ApiOperation("Получить баланс по id")
    public Balance getBalanceById(@RequestParam(value = "id") Long id) throws NotFoundException {
        return balanceService.getBalance(id);
    }

    @GetMapping("/getAllBalances")
    @ApiOperation("Получить все имеющиеся в БД балансы")
    public List<Balance> getAllBalances() {
        return balanceService.getAllBalances();
    }

    @PostMapping("/putMoney")
    @ApiOperation("Пополнить баланс на указанную сумму")
    public Balance putMoney(@RequestBody Income income) throws NotFoundException {
            return balanceService.putMoney(income.getBalanceId(), income.getIncome());
    }

    @PostMapping("/takeMoney")
    @ApiOperation("Выполнить списание средств")
    public Balance takeMoney(@RequestBody Spend spend) throws NotEnoughMoneyException, NotFoundException {
            return balanceService.takeMoney(spend.getBalanceId(), spend.getSpend());
    }
}
