package com.myProject.finalProject;

import com.myProject.finalProject.config.AbstractIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TestFinalProjectApplication {

	public static void main(String[] args) {
		var application = FinalProjectApplication.createSpringApplication();

		application.addInitializers(new AbstractIntegrationTest.Initializer());

		application.run(args);
	}

	@Test
	void contextLoads() {
	}

}
