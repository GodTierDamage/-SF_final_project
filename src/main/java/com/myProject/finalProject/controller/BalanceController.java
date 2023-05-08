package com.myProject.finalProject.controller;

import com.myProject.finalProject.entity.Balance;
import com.myProject.finalProject.entity.Income;
import com.myProject.finalProject.entity.Spend;
import com.myProject.finalProject.entity.Transaction;
import com.myProject.finalProject.exception.BalanceNotFoundException;
import com.myProject.finalProject.exception.CreateBalanceException;
import com.myProject.finalProject.exception.NotEnoughMoneyException;
import com.myProject.finalProject.service.BalanceService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "api/balance")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/getBalance")
    @ApiOperation("Получить баланс по id")
    public Balance getBalanceById(@RequestParam(name = "balanceId") Long balanceId) throws BalanceNotFoundException {
        return balanceService.getBalance(balanceId);
    }

    @PostMapping("/putMoney")
    @ApiOperation("Пополнить баланс на указанную сумму")
    public Balance putMoney(@RequestBody Income income) throws BalanceNotFoundException {
        return balanceService.putMoney(income);
    }

    @PostMapping("/takeMoney")
    @ApiOperation("Выполнить списание средств")
    public Balance takeMoney(@RequestBody Spend spend) throws NotEnoughMoneyException, BalanceNotFoundException {
        return balanceService.takeMoney(spend);
    }

    @GetMapping("/getTransactions")
    @ApiOperation("Получить список всех операций")
    public List<Transaction> getTransactions(@RequestParam(name = "balanceId") Long balanceId) {
        return balanceService.findAllTransactionsByBalanceId(balanceId);
    }

    @GetMapping("/getTransactions/before")
    @ApiOperation("Получить список всех операций до определенной даты")
    public List<Transaction> getTransactionsBeforeDate(
            @RequestParam(name = "balanceId") Long balanceId,
            @RequestParam(name = "beforeDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime beforeDate) {
        return balanceService.findByDateBefore(balanceId, Timestamp.valueOf(beforeDate));
    }

    @GetMapping("/getTransactions/after")
    @ApiOperation("Получить список всех операций до определенной даты")
    public List<Transaction> getTransactionsAfterDate(
            @RequestParam(name = "balanceId") Long balanceId,
            @RequestParam(name = "afterDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime afterDate) {
        return balanceService.findByDateAfter(balanceId, Timestamp.valueOf(afterDate));
    }

    @GetMapping("/getTransactions/between")
    @ApiOperation("Получить список всех операций до определенной даты")
    public List<Transaction> getTransactionsBetweenDate(
            @RequestParam(name = "balanceId") Long balanceId,
            @RequestParam(name = "firstDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime firstDate,
            @RequestParam(name = "secondDate") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime secondDate) {
        return balanceService.findByDateBetween(balanceId, Timestamp.valueOf(firstDate), Timestamp.valueOf(secondDate));
    }

    @PostMapping("/transferMoney")
    @ApiOperation("Перевод средств с одного баланса на другой")
    public ResponseEntity<String> transferMoney(@RequestBody Transaction transaction) {
        try {
            balanceService.transferMoney(transaction);
            return ResponseEntity.ok("Operation completed successfully");
        } catch (BalanceNotFoundException | NotEnoughMoneyException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping("/addBalance")
    @ApiOperation("Добавить в базу данных новый баланс")
    public Balance addBalance(@RequestBody Balance balance) throws CreateBalanceException {
        return balanceService.addBalance(balance);
    }
}
