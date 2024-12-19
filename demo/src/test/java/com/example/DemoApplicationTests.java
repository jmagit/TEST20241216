package com.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import com.example.domains.contracts.repositories.VetsRepository;

@Import(TestcontainersConfiguration.class)
@SpringBootTest
class DemoApplicationTests {

	@Autowired
	VetsRepository dao;
	
	@Test
	void contextLoads() {
		var actual = dao.count();
		assertEquals(6, actual);
	}
}
