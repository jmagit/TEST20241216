package com.example.domains.entities;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

class PetTest {

	@Test
	void testIsValid() {
		var item = new Pet(0, "Toto", "2020-09-07", 1, 1);
		
		assertTrue(item.isValid());
	}

	@ParameterizedTest
	@CsvSource({
//		"'',2020-09-07,ERRORES: name: el tamaño debe estar entre 2 y 30, no debe estar vacío.",
		"'   ',2020-09-07,ERRORES: name: no debe estar vacío.",
		"x,2020-09-07,ERRORES: name: el tamaño debe estar entre 2 y 30.",
		"toto,2030-09-07,ERRORES: birthDate: debe ser una fecha en el pasado o en el presente.",
	})
	void testIsInvalid(String name, String birthDate, String errorsMessage) {
		var item = new Pet(0, name, birthDate, 1, 1);
		
		assertTrue(item.isInvalid());
		assertEquals(errorsMessage, item.getErrorsMessage());
	}

}
