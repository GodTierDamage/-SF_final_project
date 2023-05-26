package com.myProject.finalProject.config;

import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.lifecycle.Startables;

import java.util.Collections;
import java.util.Map;
import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public abstract class AbstractIntegrationTest {

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13.2-alpine")
                .withUsername("postgres")
                .withPassword("postgres");

        static GenericContainer<?> api = new GenericContainer<>("final-project")
                .withExposedPorts(6379);

        public static Map<String, String> getProperties() {

            try {
                Startables.deepStart(Stream.of(api, postgres));
            return Map.of(
                    "spring.datasource.url", postgres.getJdbcUrl(),
                    "spring.datasource.username", postgres.getUsername(),
                    "spring.datasource.password",postgres.getPassword(),

                    "spring.api.host", api.getContainerIpAddress(),
                    "spring.api.port", api.getFirstMappedPort().toString()
            );
            }
            catch (Exception e) {
                e.printStackTrace();
                return Collections.emptyMap();
            }
        }

        @Override
        public void initialize(ConfigurableApplicationContext applicationContext) {
            var env = applicationContext.getEnvironment();
            env.getPropertySources().addFirst(new MapPropertySource(
                    "testcontainers", (Map) getProperties()
            ));
        }
    }
}
