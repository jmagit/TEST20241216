package com.example.ejemplos;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.InvocationTargetException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.example.testutils.PrivateMethod;
import com.example.testutils.SmokeTest;

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
			@Tag("smoke")
			void test_Suma_dos_enteros() {
//				Calculadora calculadora = new Calculadora();

				double actual = calculadora.suma(1, 2);

				assertEquals(3, actual);
				// desmont
			}

			@Test
			void test_Suma_dos_enteros_grandes() {
//				Calculadora calculadora = new Calculadora();

				int actual = calculadora.suma(Integer.MAX_VALUE, 1);

				assertEquals(Integer.MIN_VALUE, actual);
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

			@ParameterizedTest(name = "{index} => {0} + {1} = {2}")
			@CsvSource({ "1,2,3", "0.1,0.2,0.3", "-1,2,1", "1,-0.9,0.1", "0,0,0" })
			void testSumas(double operando1, double operando2, double resultado) {
				double actual = calculadora.suma(operando1, operando2);
				assertEquals(resultado, actual);
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

	@Nested
	@DisplayName("Método: Divide")
	class Divide {
		@Nested
		class OK {
			@Test
			@DisplayName("Divide dos enteros")
			void testDivideInt() {
				double actual = calculadora.divide(1, 2);

				assertEquals(0, actual);
			}

			@Test
			@DisplayName("Divide con decimales")
			void testDivideDouble() {
				double actual = calculadora.divide(1.0, 2.0);

				assertEquals(0.5, actual);
			}
		}

		@Nested
		class KO {
			@Test
			@DisplayName("Divide por 0")
			void testDivideDouble() {
				var ex = assertThrows(ArithmeticException.class, () -> calculadora.divide(1.0, 0));
				assertEquals("/ by zero", ex.getMessage());
//				try {
//					calculadora.divide(1.0, 0);
//					fail("No lanza la excepción");
//				} catch (Exception ex) {
//					assertEquals("/ by zero", ex.getMessage());
//				}
			}
//			@Test
//			@DisplayName("Divide por 0")
//			void testDivideEnteros () {
//				double actual = calculadora.divide(1, 0);
//
//				assertEquals(0.5, actual);
//			}

		}
	}

	@Test
	@SmokeTest
	void Privado() throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
		assertEquals(0.3, PrivateMethod.exec(calculadora, "roundIEEE754", new Class[] { double.class }, (0.1 + 0.2)));
		assertEquals(0.1, PrivateMethod.exec(calculadora, "roundIEEE754", new Class[] { double.class }, (1 - 0.9)));
	}
	
	@Nested
	@DisplayName("Ejemplo TDD con la kata del año bisiesto")
	class TDD {
//		@Disabled
		@DisplayName("Es bisiesto")
		@ParameterizedTest(name = "{0} por {1}")
		@CsvSource({
			"2024, ser multiplo de 4",
			"2000, ser multiplo de 400",
			})
		void bisiestoOK(int año, String caso) {
			assertTrue(calculadora.esBisiesto(año));
		}
		
//		@Disabled
		@DisplayName("No es bisiesto")
		@ParameterizedTest(name = "{0} por {1}")
		@CsvSource({
			"2023, no ser multiplo de 4",
			"1900, es multiplo de 100",
			})
		void bisiestoKO(int año, String caso) {
			assertFalse(calculadora.esBisiesto(año));
		}
	}
	@Nested
	@DisplayName("Ejemplos Mock")
	class Mokea {
		@Test
		void simulaSuma() {
			var calc = mock(Calculadora.class);
//			when(calc.suma(2, 2)).thenReturn(3).thenReturn(4).thenReturn(5);
//			when(calc.suma(21, 2)).thenReturn(33);
			when(calc.suma(anyInt(), anyInt())).thenReturn(3).thenReturn(4).thenReturn(5);
			
			var actual = calc.suma(2, 2);
			assertEquals(3, actual);
			assertEquals(4, calc.suma(2, 2));
			assertEquals(5, calc.suma(2, 2));
			assertEquals(5, calc.suma(2, 2));
			assertEquals(5, calc.suma(21, 2));
			assertEquals(5, calc.suma(2, 2));
			
			verify(calc, times(5)).suma(2, 2);
		}
	}

}
