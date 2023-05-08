package com.myProject.finalProject.configuration;

import com.myProject.finalProject.entity.Balance;
import com.myProject.finalProject.entity.Income;
import com.myProject.finalProject.entity.Spend;
import com.myProject.finalProject.entity.Transaction;
import com.myProject.finalProject.enums.OperationType;
import com.myProject.finalProject.enums.SpendingType;
import com.myProject.finalProject.repository.BalanceRepository;
import com.myProject.finalProject.repository.TransactionRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

@Configuration
public class BalanceConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(BalanceRepository balanceRepository, TransactionRepository transactionRepository) {
        return args -> {
            Balance testBalance1 = Balance.builder().amount(BigDecimal.valueOf(1000)).build();
            Balance testBalance2 = Balance.builder().amount(BigDecimal.valueOf(0)).build();
            Balance testBalance3 = Balance.builder().amount(BigDecimal.valueOf(378)).build();

            List<Transaction> transactions = List.of(
                    Spend.builder()
                            .id(1L)
                            .operationType(OperationType.SPEND)
                            .balanceId(1L)
                            .typeOfSpendingOperation(SpendingType.TRANSFER)
                            .receiverId(3L)
                            .amount(BigDecimal.valueOf(1000))
                            .dateOfOperation(Timestamp.valueOf("1931-11-24 17:53:27"))
                            .build(),
                    Spend.builder()
                            .id(2L)
                            .operationType(OperationType.SPEND)
                            .balanceId(1L)
                            .typeOfSpendingOperation(SpendingType.WITHDRAW)
                            .amount(BigDecimal.valueOf(300))
                            .dateOfOperation(Timestamp.valueOf("1975-01-04 10:13:47"))
                            .build(),
                    Spend.builder()
                            .id(3L)
                            .operationType(OperationType.SPEND)
                            .balanceId(1L)
                            .typeOfSpendingOperation(SpendingType.PURCHASE)
                            .amount(BigDecimal.valueOf(500))
                            .dateOfOperation(Timestamp.valueOf("2023-01-01 00:00:00"))
                            .build(),
                    Income.builder()
                            .operationType(OperationType.INCOME)
                            .id(4L)
                            .balanceId(1L)
                            .amount(BigDecimal.valueOf(300))
                            .dateOfOperation(Timestamp.valueOf("2003-04-22 14:22:37"))
                            .build()
            );

            balanceRepository.saveAll(
                    List.of(testBalance1, testBalance2, testBalance3)
            );

            transactionRepository.saveAll(transactions);
        };
    }
}
