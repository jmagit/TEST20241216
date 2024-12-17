package com.example.ejemplos;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("Pruebas de la clase Calculadora")
@DisplayNameGeneration(DisplayNameGenerator.ReplaceUnderscores.class)
class CalculadoraTest {
	Calculadora calculadora;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		calculadora = new Calculadora();
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Nested
	@DisplayName("Método: Suma")
	class Suma {
		@Nested
		class OK {
			@Test
			@DisplayName("Suma dos números enteros")
			void test_Suma_dos_enteros() {
//				Calculadora calculadora = new Calculadora();

				double actual = calculadora.suma(1, 2);

				assertEquals(3, actual);
				// desmont
			}

			@Test
			@DisplayName("Suma dos numeros reales")
			void testSuma1() {
//				Calculadora calculadora = new Calculadora();

				double actual = calculadora.suma(1.5, 2.5);

				assertEquals(4, actual);
//		assertEquals(0.1, calculadora.suma(1, -0.9));
//		assertEquals(0.3, calculadora.suma(0.1, 0.2));
			}
		}

		@Nested
		class KO {

			@Test
			@DisplayName("Suma dos numeros limite IEEE")
			void testSuma2() {
//				Calculadora calculadora = new Calculadora();

				double actual = calculadora.suma(0.1, 0.2);

				assertEquals(0.3, actual);
			}
		}
	}
}
