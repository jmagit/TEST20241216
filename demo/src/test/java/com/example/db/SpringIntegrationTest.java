package com.example.db;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.domains.contracts.repositories.OwnersRepository;
import com.example.domains.contracts.repositories.PetsRepository;

@Testcontainers
@SpringBootTest
class SpringIntegrationTest {
	@Container
	@ServiceConnection
	static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine").withInitScript("init-db.sql");

//	@DynamicPropertySource
//	static void configureProperties(DynamicPropertyRegistry registry) {
//		registry.add("spring.datasource.url", postgres::getJdbcUrl);
//		registry.add("spring.datasource.username", postgres::getUsername);
//		registry.add("spring.datasource.password", postgres::getPassword);
//	}

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		postgres.start();
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
		postgres.stop();
	}

	@Autowired
	OwnersRepository dao;
	
	@Test
	void testConnection() {
		var actual = dao.count();
		assertEquals(10, actual);
	}
}
