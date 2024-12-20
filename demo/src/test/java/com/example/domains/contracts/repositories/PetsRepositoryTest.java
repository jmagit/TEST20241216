package com.example.domains.contracts.repositories;

import static org.junit.Assume.assumeTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.context.TestPropertySource;

import com.example.domains.entities.Pet;
import com.example.domains.entities.Type;
import com.example.testutils.IntegrationTest;

@IntegrationTest
@DisplayName("PetsRepository: Pruebas de integración")
@DataJpaTest
@TestPropertySource(properties = {
  "spring.test.database.replace=none",
  "spring.datasource.url=jdbc:tc:postgresql:17-alpine:///test?TC_INITSCRIPT=init-db.sql"
})
//@SpringBootTest(properties = {"spring.datasource.url=jdbc:tc:postgresql:17-alpine:///test?TC_INITSCRIPT=init-db.sql"})
class PetsRepositoryTest {

	@Autowired
	PetsRepository dao;

	@Test
	void testFindAll() {
		var obtenido = dao.findAll();
		
		assertNotNull(obtenido);
		assertEquals(13, obtenido.size());
	}

	@Test
	void testFindByIdExists() {
		var obtenido = dao.findById(8).orElseGet(() -> fail("Empty"));
		
		assertAll("Propiedades", 
				() -> assertEquals(8, obtenido.getId()),
				() -> assertEquals("Max", obtenido.getName(), "Name"),
				() -> assertEquals(toDate("1995-09-04"), obtenido.getBirthDate().get(), "BirthDate"),
				() -> assertEquals("cat", obtenido.getType().getName(), "Type"),
				() -> assertEquals("Jean", obtenido.getOwner().getFirstName(), "Owner"),
				() -> assertEquals(2, obtenido.getVisits().size()));
	}

	@Test
	void testFindByIdNotExists() {
		var obtenido = dao.findById(88);
		assertTrue(obtenido.isEmpty());
	}

	@Test
	void testExistsByIdTrue() {
		var obtenido = dao.existsById(8);
		assertTrue(obtenido);
	}

	@Test
	void testExistsByIdFalse() {
		var obtenido = dao.existsById(88);
		assertFalse(obtenido);
	}

	@Test
	void testSaveAdd() {
		var item = Pet.builder()
				.name("Toto")
				.birthDate(toDate("2020-09-07"))
//				.type(new Type(1))
				.build();
		assumeTrue("Prueba cancelada por datos invalidos", item.isValid());
		Pet obtenido;
		try {
			obtenido = dao.save(item);
		} catch (Exception ex) {
			throw new AssertionError("Unexpected exception thrown: " + ex.toString(), ex);
		}
		assertAll("Propiedades", 
				() -> assertEquals(14, obtenido.getId()),
				() -> assertEquals("Toto", obtenido.getName(), "Name"),
				() -> assertEquals(toDate("2020-09-07"), obtenido.getBirthDate().get(), "BirthDate"));
	}

	@Test
	void testSaveAddSkip() {
		var item = Pet.builder()
				.name("")
				.birthDate(toDate("2020-09-07"))
//				.type(new Type(1))
				.build();
		assumeTrue("Prueba cancelada por datos invalidos", item.isValid());
		Pet obtenido;
		try {
			obtenido = dao.save(item);
		} catch (Exception ex) {
			throw new AssertionError("Unexpected exception thrown: " + ex.toString(), ex);
		}
		assertAll("Propiedades", 
				() -> assertEquals(14, obtenido.getId()),
				() -> assertEquals("Toto", obtenido.getName(), "Name"),
				() -> assertEquals(toDate("2020-09-07"), obtenido.getBirthDate().get(), "BirthDate"));
	}
	
	@Test
	void testSaveUpdate() {
		var item = Pet.builder()
				.id(4)
				.name("Princesa")
				.birthDate(toDate("2022-02-22"))
				.type(new Type(1))
				.build();
		assumeTrue("Prueba cancelada por datos invalidos", item.isValid());

		Pet obtenido;
		try {
			obtenido = dao.save(item);
		} catch (Exception ex) {
			throw new AssertionError("Unexpected exception thrown: " + ex.toString(), ex);
		}
		
		assertAll("Propiedades", 
				() -> assertEquals(4, obtenido.getId()),
				() -> assertEquals("Princesa", obtenido.getName(), "Name"),
				() -> assertEquals(toDate("2022-02-22"), obtenido.getBirthDate().get(), "BirthDate"),
				() -> assertEquals("cat", obtenido.getType().getName(), "Type")
				);
	}
	
	@Test
	void testSaveNotExists() {
		var item = Pet.builder()
				.id(33)
				.name("Leo")
				.birthDate(toDate("2020-09-07"))
				.type(new Type(1))
				.build();
		assumeTrue("Prueba cancelada por datos invalidos", item.isValid());
		assertThrows(ObjectOptimisticLockingFailureException.class, () -> dao.save(item));
	}

	@Test
	void testDeleteExists() {
		assertTrue(dao.existsById(10));
		assertDoesNotThrow(() -> dao.delete(Pet.builder().id(10).build()));
		assertFalse(dao.existsById(10));
	}
	@Test
	void testDeleteNotExists() {
		assertFalse(dao.existsById(99));
		assertDoesNotThrow(() -> dao.delete(Pet.builder().id(99).build()));
		assertFalse(dao.existsById(99));
	}

	@Test
	void testDeleteByIdExists() {
		assertTrue(dao.existsById(9));
		assertDoesNotThrow(() -> dao.deleteById(9));
		assertFalse(dao.existsById(9));
	}
	
	@Test
	void testDeleteByIdNotExists() {
		assertFalse(dao.existsById(99));
		assertDoesNotThrow(() -> dao.deleteById(99));
		assertFalse(dao.existsById(99));
	}

	@Test
	void testDeleteByIdException() {
		assertTrue(dao.existsById(7));
		try {
			dao.deleteById(7);
		} catch (Exception ex) {
			throw new AssertionError("Unexpected exception thrown: " + ex.toString(), ex);
		}
//		assertThrows(Throwable.class, () -> dao.deleteById(7));
//		assertThrows(DataIntegrityViolationException.class, () -> dao.deleteById(7));
//		assertDoesNotThrow(() -> dao.deleteById(7));
	}

	private static Date toDate(String date) {
		try {
			return (new SimpleDateFormat("yyyy-MM-dd")).parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
