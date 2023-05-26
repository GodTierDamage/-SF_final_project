package com.myProject.finalProject;

import com.myProject.finalProject.entity.Transaction;
import com.myProject.finalProject.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Testcontainers
public class MyIntegrationTests {

    @Autowired
    private TransactionRepository repository;

    @Container
    private static PostgreSQLContainer container = new PostgreSQLContainer("postgres:13.2-alpine")
            .withUsername("postgres")
            .withPassword("postgres");

    @Container
    private static GenericContainer genericContainer = new GenericContainer("final-project")
            .withExposedPorts(8080);

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", container::getJdbcUrl);
        registry.add("spring.datasource.username", container::getUsername);
        registry.add("spring.datasource.password", container::getPassword);
    }

    @Test
    public void findAllTransactions() {
        List<Transaction> transactions = repository.findAll();
        assertThat(transactions.size()).isEqualTo(4);
    }

}
