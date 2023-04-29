package com.myProject.finalProject.service;

import com.myProject.finalProject.entity.Balance;
import com.myProject.finalProject.entity.Income;
import com.myProject.finalProject.entity.Spend;
import com.myProject.finalProject.enums.OperationType;
import com.myProject.finalProject.exception.BalanceNotFoundException;
import com.myProject.finalProject.exception.NotEnoughMoneyException;
import com.myProject.finalProject.repository.BalanceRepository;
import com.myProject.finalProject.repository.IncomeRepository;
import com.myProject.finalProject.repository.SpendRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource(locations = "classpath:application.yml")
class BalanceServiceTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BalanceRepository balanceRepository;

    @Autowired
    private IncomeRepository incomeRepository;

    @Autowired
    private SpendRepository spendRepository;

    private BalanceService balanceService;

    @BeforeEach
    void setup() {
        balanceService = new BalanceService(balanceRepository, incomeRepository, spendRepository);
    }

    @SneakyThrows
    @Test
    void testPutMoneySuccess() {
        Balance balance = Balance.builder()
                .balance(BigDecimal.valueOf(1000))
                .build();

        entityManager.persist(balance);

        Income income = Income.builder()
                .balanceId(balance.getId())
                .operationType(OperationType.INCOME)
                .income(BigDecimal.valueOf(500))
                .build();

        entityManager.persist(income);

        Balance result = balanceService.putMoney(income);

        assertThat(result).isNotNull();
        assertThat(result.getBalance()).isEqualTo(BigDecimal.valueOf(1500));
    }

    @Test
    void testPutMoneyWhenBalanceWithIdDoesntExist() {
        Balance balance = Balance.builder()
                .balance(BigDecimal.valueOf(1000))
                .build();

        entityManager.persist(balance);

        Income income = Income.builder()
                .balanceId(2L)
                .operationType(OperationType.INCOME)
                .income(BigDecimal.valueOf(500))
                .build();

        entityManager.persist(income);

        assertThatThrownBy(() -> balanceService.putMoney(income))
                .isInstanceOf(BalanceNotFoundException.class)
                .hasMessage("Balance with id " + income.getBalanceId() + " doesn't exist");
    }

    @SneakyThrows
    @Test
    void testTakeMoneySuccess() {
        Balance balance = Balance.builder()
                .balance(BigDecimal.valueOf(1000))
                .build();
        entityManager.persist(balance);

        Spend spend = Spend.builder()
                .balanceId(balance.getId())
                .operationType(OperationType.SPEND)
                .spend(BigDecimal.valueOf(999))
                .build();
        entityManager.persist(spend);

        Balance result = balanceService.takeMoney(spend);

        assertThat(result).isNotNull();
        assertThat(result.getBalance()).isEqualTo(BigDecimal.valueOf(1));
    }

    @Test
    void testTakeMoneyWhenBalanceHasNotEnoughMoney() {
        Balance balance = Balance.builder()
                .balance(BigDecimal.valueOf(1000))
                .build();
        entityManager.persist(balance);

        Spend spend = Spend.builder()
                .balanceId(balance.getId())
                .operationType(OperationType.SPEND)
                .spend(BigDecimal.valueOf(1001))
                .build();
        entityManager.persist(spend);

        assertThatThrownBy(() -> balanceService.takeMoney(spend))
                .isInstanceOf(NotEnoughMoneyException.class)
                .hasMessage("Balance with id " + spend.getBalanceId() + " has not enough money");
    }

    @Test
    void testTakeMoneyWhenBalanceWithIdDoesntExist() {
        Balance balance = Balance.builder()
                .balance(BigDecimal.valueOf(1000))
                .build();
        entityManager.persist(balance);

        Spend spend = Spend.builder()
                .balanceId(2L)
                .operationType(OperationType.SPEND)
                .spend(BigDecimal.valueOf(999))
                .build();
        entityManager.persist(spend);

        assertThatThrownBy(() -> balanceService.takeMoney(spend))
                .isInstanceOf(BalanceNotFoundException.class)
                .hasMessage("Balance with id " + spend.getBalanceId() + " doesn't exist");
    }

    @SneakyThrows
    @Test
    void testGetBalanceSuccess() {
        Balance balance = Balance.builder()
                .balance(BigDecimal.valueOf(1000))
                .build();
        entityManager.persist(balance);

        Balance result = balanceService.getBalance(balance.getId());

        assertThat(result).isNotNull();
        assertThat(result).isEqualTo(balance);
    }

    @Test
    void testGetBalanceWhenBalanceWithIdDoesntExist() {
        Balance balance = Balance.builder()
                .balance(BigDecimal.valueOf(1000))
                .build();
        entityManager.persist(balance);

        long wrongBalanceId = 2L;

        assertThatThrownBy(() -> balanceService.getBalance(wrongBalanceId))
                .isInstanceOf(BalanceNotFoundException.class)
                .hasMessage("Balance with id " + wrongBalanceId + " doesn't exist");
    }
}