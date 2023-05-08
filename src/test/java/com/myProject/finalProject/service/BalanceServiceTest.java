package com.myProject.finalProject.service;

import com.myProject.finalProject.entity.Balance;
import com.myProject.finalProject.entity.Income;
import com.myProject.finalProject.entity.Spend;
import com.myProject.finalProject.entity.Transaction;
import com.myProject.finalProject.enums.OperationType;
import com.myProject.finalProject.enums.SpendingType;
import com.myProject.finalProject.exception.BalanceNotFoundException;
import com.myProject.finalProject.exception.NotEnoughMoneyException;
import com.myProject.finalProject.repository.*;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.yml")
@ExtendWith(SpringExtension.class)
class BalanceServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    private BalanceService balanceService;

    @BeforeEach
    @Transactional
    void setup() {
        balanceService = new BalanceService(balanceRepository, transactionRepository);
    }

    @SneakyThrows
    @Test
    void testPutMoneySuccess() {
        Balance balance = createBalance();

        entityManager.persist(balance);

        Income income = createIncome(balance.id());

        entityManager.persist(income);

        Balance result = balanceService.putMoney(income);

        assertThat(result).isNotNull();
        assertThat(result.amount()).isEqualTo(BigDecimal.valueOf(1500));
    }

    private Balance createBalance() {
        return Balance.builder()
                .amount(BigDecimal.valueOf(1000))
                .build();
    }

    private Income createIncome(Long balanceId) {
        return Income.builder()
                .balanceId(balanceId)
                .operationType(OperationType.INCOME)
                .amount(BigDecimal.valueOf(500))
                .build();
    }

    @Test
    void testPutMoneyWhenBalanceWithIdDoesntExist() {
        Balance balance = createBalance();

        entityManager.persist(balance);

        Income income = createIncome(2L);

        entityManager.persist(income);

        assertThatThrownBy(() -> balanceService.putMoney(income))
                .isInstanceOf(BalanceNotFoundException.class)
                .hasMessage("Balance with id " + income.balanceId() + " doesn't exist");
    }

    @SneakyThrows
    @Test
    void testTakeMoneySuccess() {
        Balance balance = createBalance();
        entityManager.persist(balance);

        Transaction spend = createSpend(balance.id());
        entityManager.persist(spend);

        Balance result = balanceService.takeMoney(spend);

        assertThat(result).isNotNull();
        assertThat(result.amount()).isEqualTo(BigDecimal.valueOf(1));

    }

    private Spend createSpend(Long balanceId) {
        return Spend.builder()
                .operationType(OperationType.SPEND)
                .balanceId(balanceId)
                .amount(BigDecimal.valueOf(999))
                .build();
    }

    @Test
    void testTakeMoneyWhenBalanceHasNotEnoughMoney() {
        Balance balance = createBalance();
        balance.amount(BigDecimal.ZERO);

        entityManager.persist(balance);

        Spend spend = createSpend(balance.id());
        entityManager.persist(spend);

        assertThatThrownBy(() -> balanceService.takeMoney(spend))
                .isInstanceOf(NotEnoughMoneyException.class)
                .hasMessage("Balance with id " + spend.balanceId() + " has not enough money");
    }

    @Test
    void testTakeMoneyWhenBalanceWithIdDoesntExist() {
        Balance balance = createBalance();

        entityManager.persist(balance);

        Spend spend = createSpend(2L);

        entityManager.persist(spend);

        assertThatThrownBy(() -> balanceService.takeMoney(spend))
                .isInstanceOf(BalanceNotFoundException.class)
                .hasMessage("Balance with id " + spend.balanceId() + " doesn't exist");
    }

    @SneakyThrows
    @Test
    void testGetBalanceSuccess() {
        Balance balance = createBalance();

        entityManager.persist(balance);

        Balance result = balanceService.getBalance(balance.id());

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(balance);
    }

    @Test
    void testGetBalanceWhenBalanceWithIdDoesntExist() {
        Balance balance = createBalance();

        entityManager.persist(balance);

        long wrongBalanceId = 2L;

        assertThatThrownBy(() -> balanceService.getBalance(wrongBalanceId))
                .isInstanceOf(BalanceNotFoundException.class)
                .hasMessage("Balance with id " + wrongBalanceId + " doesn't exist");
    }

    @Test
    void testFindAllTransactionsByBalanceId() {
        List<Transaction> transactions = createListOfTransactions();

        transactions.forEach(transaction -> entityManager.persistAndFlush(transaction));

        List<Transaction> result = balanceService.findAllTransactionsByBalanceId(1L);

        result.forEach(System.out::println);

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2L);

        result.forEach(System.out::println);
    }

    private List<Transaction> createListOfTransactions() {
        return List.of(
                Income.builder()
                        .balanceId(1L)
                        .amount(BigDecimal.valueOf(1000))
                        .dateOfOperation(Timestamp.valueOf("2021-01-13 11:53:40"))
                        .build(),
                Income.builder()
                        .balanceId(2L)
                        .amount(BigDecimal.valueOf(1000))
                        .dateOfOperation(new Timestamp(System.currentTimeMillis()))
                        .build(),
                Spend.builder()
                        .balanceId(1L)
                        .amount(BigDecimal.valueOf(1000))
                        .dateOfOperation(Timestamp.valueOf("1961-04-12 09:07:00"))
                        .build(),
                Spend.builder()
                        .balanceId(3L)
                        .amount(BigDecimal.valueOf(1000))
                        .dateOfOperation(Timestamp.valueOf("1991-09-17 11:07:13"))
                        .build()
        );
    }

    @Test
    void testFindByBalanceIdAndDateBefore() {
        List<Transaction> transactions = createListOfTransactions();

        transactions.forEach(transaction -> entityManager.persistAndFlush(transaction));

        List<Transaction> result = balanceService.findByDateBefore(1L, Timestamp.valueOf("2000-01-01 00:00:00"));

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);

        result.forEach(System.out::println);

    }

    @Test
    void testFindByBalanceIdAndDateBetween() {
        List<Transaction> transactions = createListOfTransactions();

        transactions.forEach(transaction -> entityManager.persistAndFlush(transaction));

        List<Transaction> result =
                balanceService.findByDateBetween(1L,
                        Timestamp.valueOf("1960-01-01 00:00:01"),
                        new Timestamp(System.currentTimeMillis()));

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(2);

        result.forEach(System.out::println);
    }

    @Test
    void testFindByBalanceIdAndDateAfter() {
        List<Transaction> transactions = createListOfTransactions();

        transactions.forEach(transaction -> entityManager.persistAndFlush(transaction));

        List<Transaction> result = balanceService.findByDateAfter(1L, Timestamp.valueOf("2000-01-01 00:00:01"));

        assertThat(result).isNotNull();
        assertThat(result.size()).isEqualTo(1);

        result.forEach(System.out::println);
    }

    @SneakyThrows
    @Test
    void testTransferMoneySuccess() {
        Balance sender = createBalance();
        Balance receiver = createBalance();

        entityManager.persistAndFlush(sender);
        entityManager.persistAndFlush(receiver);

        Transaction transfer = createSpend(sender.id())
                .receiverId(receiver.id())
                .typeOfSpendingOperation(SpendingType.TRANSFER);

        assertThat(sender.amount()).isNotEqualTo(BigDecimal.ZERO);

        transfer.amount(sender.amount());

        BigDecimal resultAmount = receiver.amount().add(sender.amount());
        boolean result = balanceService.transferMoney(transfer);

        assertThat(result).isTrue();

        Balance resultSender = balanceService.getBalance(sender.id());
        Balance resultReceiver = balanceService.getBalance(receiver.id());

        assertThat(resultSender.amount()).isEqualTo(BigDecimal.ZERO);
        assertThat(resultReceiver.amount()).isEqualTo(resultAmount);
    }

    @Test
    public void testTransferMoneyWhenTypeOfSpendingOperationIsWrong() {
        Balance sender = createBalance();
        Balance receiver = createBalance();

        entityManager.persistAndFlush(sender);
        entityManager.persistAndFlush(receiver);

        Transaction transfer = createIncome(sender.id());

        assertThatThrownBy(() -> balanceService.transferMoney(transfer))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Illegal type of operationType for transfer");
    }

    @Test
    public void justTest() {
        Transaction spend = createSpend(1L);
        assertThat(spend.operationType()).isEqualTo(OperationType.SPEND);
    }

}