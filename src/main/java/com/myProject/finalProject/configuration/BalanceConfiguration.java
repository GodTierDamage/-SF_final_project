package com.myProject.finalProject.configuration;

import com.myProject.finalProject.entity.Balance;
import com.myProject.finalProject.repository.BalanceRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class BalanceConfiguration {

    @Bean
    CommandLineRunner commandLineRunner(BalanceRepository repository) {
        return args -> {
            Balance testBalance1 = Balance.builder().balance(BigDecimal.valueOf(1000)).build();
            Balance testBalance2 = Balance.builder().balance(BigDecimal.valueOf(0)).build();
            Balance testBalance3 = Balance.builder().balance(BigDecimal.valueOf(378)).build();

            repository.saveAll(
                    List.of(testBalance1, testBalance2, testBalance3)
            );
        };
    }
}
