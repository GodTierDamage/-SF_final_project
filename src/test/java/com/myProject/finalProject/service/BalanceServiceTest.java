package com.myProject.finalProject.service;

import com.myProject.finalProject.entity.Balance;
import com.myProject.finalProject.repository.BalanceRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class BalanceServiceTest {

    @Mock
    private BalanceRepository repository;
    private BalanceService balanceService;

    @BeforeEach
    void setup() {
        balanceService = new BalanceService(repository);
    }

    @SneakyThrows
    @Test
    @DisplayName("Testing method getBalance")
    public void canGetBalance() {
        given(repository.findById(1L)).willReturn(Optional.of(Balance.builder().build()));
        balanceService.getBalance(1L);
        verify(repository).findById(1L);
    }

    @SneakyThrows
    @Test
    @DisplayName("Testing method putMoney")
    public void canPutMoney() {
        Balance testBalance = Balance.builder().id(1L).balance(BigDecimal.valueOf(1000)).build();

        given(repository.findById(testBalance.getId())).willReturn(Optional.of(testBalance));
        given(repository.save(testBalance)).willReturn(testBalance);

        balanceService.putMoney(testBalance.getId(), BigDecimal.valueOf(100));

        ArgumentCaptor<Balance> argumentCaptor = ArgumentCaptor.forClass(Balance.class);

        verify(repository).save(argumentCaptor.capture());
    }

    @SneakyThrows
    @Test
    @DisplayName("Testing method takeMoney")
    public void canTakeMoney() {
        Balance testBalance = Balance.builder().build().setBalance(new BigDecimal(1000));

        given(repository.findById(testBalance.getId())).willReturn(Optional.of(testBalance));

        balanceService.takeMoney(testBalance.getId(), BigDecimal.valueOf(100));

        ArgumentCaptor<Balance> argumentCaptor = ArgumentCaptor.forClass(Balance.class);

        verify(repository).save(argumentCaptor.capture());

    }
}