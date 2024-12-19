package com.example.domains.entities;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.hasProperty;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.CALLS_REAL_METHODS;
import static org.mockito.Mockito.mockStatic;

import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.RepetitionInfo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

import com.example.testutils.SmokeTest;

class PersonaTest {

	@Nested
	@DisplayName("Crear persona")
	@TestMethodOrder(OrderAnnotation.class)
	class CrearPersona {
		@Test
//		@Tag("Smoke")
		@SmokeTest
		void test() {
			var actual = new Persona(0, "Pepito", "Grillo", LocalDate.of(2000, 12, 21));

			assertNotNull(actual);
//			assertEquals(0, actual.getId());
//			assertEquals("Pepito", actual.getNombre(), "nombre");
//			assertEquals("Grillo", actual.getApellidos().get(), "apellidos");

			assertAll("Propiedades", () -> assertEquals(0, actual.getId()),
					() -> assertEquals("Pepito", actual.getNombre(), "nombre"),
//				() -> assertTrue(actual.getApellidos().isPresent()),
//				() -> assertEquals("Grillo", actual.getApellidos().get(), "apellidos"),
					() -> assertEquals("Grillo", actual.getApellidos().orElse(null), "apellidos"),
					() -> assertEquals(23, actual.getEdad()));
			assertThat(actual,
					allOf(hasProperty("id", greaterThanOrEqualTo(0)), hasProperty("nombre", equalTo("Pepito")),
							hasProperty("apellidos", equalTo(Optional.of("Grillo")))));
//			assumeFalse("Pendiente de terminar", true);
		}

		@Test
		@Order(1)
		void testEdad() {
			var actual = new Persona(0, "Pepito", "Grillo", LocalDate.of(2000, 12, 19));
			LocalDate hoy = LocalDate.of(2024, 12, 18);
			try (MockedStatic<LocalDate> mocked = mockStatic(LocalDate.class, CALLS_REAL_METHODS)) {
				mocked.when(LocalDate::now).thenReturn(hoy);
				assertEquals(23, actual.getEdad());
			}
//			MockedStatic<LocalDate> mocked = mockStatic(LocalDate.class, CALLS_REAL_METHODS);
//			mocked.when(LocalDate::now).thenReturn(hoy);
//			assertEquals(232, actual.getEdad());
//			mocked.close();
		}
		@Order(2)
		@Test
		@Disabled("porque es tempo dependiente")
		void testEdadSinMock() {
			var actual = new Persona(0, "Pepito", "Grillo", LocalDate.of(2000, 12, 19));
			assertEquals(23, actual.getEdad());
		}

		@ParameterizedTest(name = "\"{0}\"")
		@ValueSource(strings = {"pepe", "Pepeillo", "PP"})
		@SmokeTest
		void ponNombreOK(String nombre, TestReporter testReporter) {
			var actual = new Persona(0, nombre, "Grillo", LocalDate.of(2000, 12, 22));
			assertEquals(nombre, actual.getNombre());
			testReporter.publishEntry("nombre", nombre);
		}

		@ParameterizedTest(name = "\"{0}\"")
		@ValueSource(strings = {"   "})
		@NullAndEmptySource
		void ponNombreKO(String nombre) {
			assertThrows(IllegalArgumentException.class, () -> new Persona(0, nombre, "Grillo", LocalDate.of(2000, 12, 22)));
		}

		@Test
		void testCreaPersonaSoloConNombre() {
			var actual = Persona.creaPersona("Pepito", "Grillo");

			assertEquals(actual, new Persona(0, "Otro", "tio"));
//			assertEquals(actual.toString(), (new Persona(0, "Pepito", "Grillo")).toString());
		}

		@RepeatedTest(value = 3, name = "{displayName} {currentRepetition}/{totalRepetitions}")
		void CreaPersona(RepetitionInfo repetitionInfo) {
			var actual = Persona.creaPersona("Pepito", "Grillo");

			assertEquals(actual, new Persona(0, "Otro", "tio"));
		}

	}
}
