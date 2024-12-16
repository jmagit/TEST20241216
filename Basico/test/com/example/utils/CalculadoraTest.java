package com.example.utils;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CalculadoraTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testSuma() {
		Calculadora calculadora = new Calculadora();
		
		double actual = calculadora.suma(1.5, 2.5);
		
		assertEquals(4, actual);
//		assertEquals(0.1, calculadora.suma(1, -0.9));
//		assertEquals(0.3, calculadora.suma(0.1, 0.2));
	}

}
