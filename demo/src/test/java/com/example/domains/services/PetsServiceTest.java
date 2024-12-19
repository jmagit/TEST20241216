package com.example.domains.services;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.ClassOrderer;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestClassOrder;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import com.example.domains.entities.Pet;
import com.example.domains.contracts.repositories.PetsRepository;
import com.example.domains.contracts.services.PetsService;
import com.example.exceptions.DuplicateKeyException;
import com.example.exceptions.InvalidDataException;
import com.example.exceptions.NotFoundException;
import com.example.testutils.IntegrationTest;
import com.example.testutils.UnitTest;

class PetsServiceTest {
	@Nested
	@UnitTest
	@DisplayName("Pruebas unitarias")
	class Unit {
		PetsRepository dao;
		PetsService srv;
		List<Pet> listaFake;
		
		@BeforeEach
		void setUp(TestReporter out) throws Exception {
			listaFake = new ArrayList<Pet>(Arrays.asList(
					new Pet(1, "Toto", "2020-09-07"),
					new Pet(2, "Leo", "2019-02-11"), 
					new Pet(3, "Chita", "2001-12-31"),
					new Pet(4, "Lassie","2022-08-15")
					));
			dao = mock(PetsRepository.class);
			srv = new PetsServiceImpl(dao);
		}
		
		@Nested
		@Order(1)
		@DisplayName("Casos Validos")
		class OK {
			@Test
			@DisplayName("ctor: crea una instancia del servicio")
			void testCtor_PetsService() {
				assertNotNull(srv);
			}
			
			@Test
			@DisplayName("getAll: recupera multiples entidades")
			void testGetAll_isNotEmpty(TestReporter out) {
				when(dao.findAll()).thenReturn(listaFake);

				var obtenido = srv.getAll();
				
				assertNotNull(obtenido);
				assertEquals(4, obtenido.size());
				verify(dao, times(1)).findAll();
				obtenido.forEach(item -> out.publishEntry(item.toString()));
			}
			@Test
			@DisplayName("getAll: recupera 0 entidades porque esta vacía")
			void testGetAll_isEmpty() {
				when(dao.findAll()).thenReturn(new ArrayList<Pet>());
				
				var obtenido = srv.getAll();
				
				assertNotNull(obtenido);
				assertEquals(0, obtenido.size());
				verify(dao, times(1)).findAll();
			}
		
			@Test
			@DisplayName("getOne: recupera la entidad por su ID")
			void testGetOne() {
				when(dao.findById(1)).thenReturn(Optional.of(new Pet(1, "Toto", "2020-09-07")));
				
				var obtenido = srv.getOne(1).orElseGet(() -> fail("Empty"));
				
				assertAll("Propiedades", 
						() -> assertEquals(1, obtenido.getId()),
						() -> assertEquals("Toto", obtenido.getName(), "Name"),
						() -> assertEquals(toDate("2020-09-07"), obtenido.getBirthDate().get(), "BirthDate"));
			}
	
			@Test
			@DisplayName("add: añade una entidad sin ID")
			void testAdd_withoutKey() {
				var item = new Pet(0, "Toto", "2020-09-07");
				when(dao.save(any(Pet.class))).thenReturn(new Pet(1, "Toto", "2020-09-07"));

				var obtenido = assertDoesNotThrow(() -> srv.add(item));

				assertNotNull(obtenido);
				assertAll("Propiedades", 
						() -> assertEquals(1, obtenido.getId()),
						() -> assertEquals("Toto", obtenido.getName(), "Name"),
						() -> assertEquals(toDate("2020-09-07"), obtenido.getBirthDate().get(), "BirthDate"));
			}
			
			@Test
			@DisplayName("add: añade una entidad con ID")
			void testAdd_withKey() {
				var item = new Pet(1, "Toto", "2020-09-07");
				when(dao.existsById(1)).thenReturn(false);
				when(dao.save(any(Pet.class))).thenReturn(new Pet(1, "Toto", "2020-09-07"));

				var obtenido = assertDoesNotThrow(() -> srv.add(item));

				assertNotNull(obtenido);
				assertAll("Propiedades", 
						() -> assertEquals(1, obtenido.getId()),
						() -> assertEquals("Toto", obtenido.getName(), "Name"),
						() -> assertEquals(toDate("2020-09-07"), obtenido.getBirthDate().get(), "BirthDate"));
			}
			
			@Test
			@DisplayName("modify: modifica una entidad existente")
			void testModify() {
				var item = new Pet(1, "Toto", "2020-09-07");
				when(dao.existsById(1)).thenReturn(true);
				when(dao.findById(1)).thenReturn(Optional.of(new Pet(1, "Toto", "2020-09-07")));
				when(dao.save(any(Pet.class))).thenReturn(item);
				
				var obtenido = assertDoesNotThrow(() -> srv.modify(item));
				
				assertAll("Propiedades", 
						() -> assertEquals(1, obtenido.getId()),
						() -> assertEquals("Toto", obtenido.getName(), "Name"),
						() -> assertEquals(toDate("2020-09-07"), obtenido.getBirthDate().get(), "BirthDate"));
			}
			
			@Test
			@DisplayName("delete: borra una entidad existente")
			void testDelete() {
				var item = new Pet(1, "Toto", "2020-09-07");
				doNothing().when(dao).delete(item);

				assertDoesNotThrow(() -> srv.delete(item));
			}

			@Test
			@DisplayName("deleteById: borra un ID existente")
			void testDeleteById() {
				doNothing().when(dao).deleteById(1);

				assertDoesNotThrow(() -> srv.deleteById(1));
			}

		}
		
		@Nested
		@DisplayName("Casos Inválidos")
		class KO {
			@Test
			@DisplayName("ctor: lanza una IllegalArgumentException al crear una instancia del servicio sin dao")
			void testCtor_PetsService() {
				var ex = assertThrows(IllegalArgumentException.class, () -> new PetsServiceImpl(null));
				assertEquals("dao is null", ex.getMessage());
			}
			
			@Test
			@DisplayName("getOne: recupera un Optional.empty cuando no encuentra el ID")
			void testGetOne() {
				when(dao.findById(2)).thenReturn(Optional.empty());
				
				var obtenido = srv.getOne(2);
				
				assertTrue(obtenido.isEmpty());
				verify(dao, times(1)).findById(2);
			}
	
			@Test
			@DisplayName("add: lanza una InvalidDataException si no le pasan la entidad a añadir")
			void testAdd_Null() {
				var ex = assertThrows(InvalidDataException.class, () -> srv.add(null));
				assertEquals("No puede ser nulo", ex.getMessage());
			}
	
			@Test
			@DisplayName("add: lanza una InvalidDataException si le pasan una entidad con datos inválidos")
			void testAdd_InvalidData() {
				var item = mock(Pet.class);
				when(item.isInvalid()).thenReturn(true);
				when(item.isValid()).thenReturn(false);
				when(item.getErrorsMessage()).thenReturn("error forzado");
				
				var ex = assertThrows(InvalidDataException.class, () -> srv.add(item));
				
				assertEquals("error forzado", ex.getMessage());
				verify(dao, never()).save(item);
			}
	
			@Test
			@DisplayName("add: lanza una DuplicateKeyException si se intenta añadir una entidad que ya existe")
			void testAdd_Duplicate() {
				when(dao.existsById(1)).thenReturn(true);
				var item = new Pet(1, "Toto", "2020-09-07");
				
				assertThrows(DuplicateKeyException.class, () -> srv.add(item));
				
				verify(dao, never()).save(item);
			}
			
			@Test
			@DisplayName("modify: lanza una InvalidDataException si no le pasan la entidad a modificar")
			void testModify_Null() {
				var ex = assertThrows(InvalidDataException.class, () -> srv.modify(null));
				assertEquals("No puede ser nulo", ex.getMessage());
			}
	
			@Test
			@DisplayName("modify: lanza una InvalidDataException si le pasan una entidad con datos inválidos")
			void testModify_InvalidData() {
				var item = mock(Pet.class);
				when(item.isInvalid()).thenReturn(true);
				when(item.isValid()).thenReturn(false);
				when(item.getErrorsMessage()).thenReturn("error forzado");
				
				var ex = assertThrows(InvalidDataException.class, () -> srv.modify(item));
				
				assertEquals("error forzado", ex.getMessage());
				verify(dao, times(0)).save(item);
			}
	
			@Test
			@DisplayName("modify: lanza una NotFoundException si se intenta modificar una entidad que no existe")
			void testModify_NotFoundException() {
				when(dao.existsById(1)).thenReturn(false);
				var item = new Pet(1, "Toto", "2020-09-07");
				
				assertThrows(NotFoundException.class, () -> srv.modify(item));
				
				verify(dao, times(0)).save(item);
			}
			
			@Test
			@DisplayName("delete: lanza una InvalidDataException si no le pasan la entidad a borrar")
			void testDelete_Null() {
				var ex = assertThrows(InvalidDataException.class, () -> srv.delete(null));
				assertEquals("No puede ser nulo", ex.getMessage());
			}
	
		}
	}
	
	@Nested
	@IntegrationTest
	@DisplayName("Pruebas de integración")
	@Testcontainers
	@SpringBootTest
	@TestMethodOrder(OrderAnnotation.class)
	@TestClassOrder(ClassOrderer.OrderAnnotation.class)
	class Integration {
		@Container
		@ServiceConnection
		static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine").withInitScript("init-db.sql");

		@Autowired
		PetsService srv;
		
		@BeforeAll
		static void setUpBeforeClass() throws Exception {
			postgres.start();
		}

		@AfterAll
		static void tearDownAfterClass() throws Exception {
			postgres.stop();
		}
		
//		@BeforeEach
//		void setUp() throws Exception {
//			postgres.start();
//		}
//
//		@AfterEach
//		void tearDown() throws Exception {
//			postgres.stop();
//		}
		
		@Nested
		@Order(1)
		@DisplayName("Casos Validos")
		@TestMethodOrder(OrderAnnotation.class)
		class OK {
			@Test
			@Order(1)
			@DisplayName("ctor: crea una instancia del servicio")
			void testCtor_PetsService() {
				assertNotNull(srv);
			}
			
			@Test
			@Order(1) // Apesta
			@DisplayName("getAll: recupera multiples entidades")
			void testGetAll_isNotEmpty() {
				var obtenido = srv.getAll();
				
				assertNotNull(obtenido);
				assertEquals(13, obtenido.size());
			}
//			@Test
//			@DisplayName("getAll: recupera 0 entidades porque esta vacía")
//			void testGetAll_isEmpty() {
//				var obtenido = srv.getAll();
//				
//				assertNotNull(obtenido);
//				assertEquals(0, obtenido.size());
//			}
		
			@Test
			@Order(2) // Apesta
			@DisplayName("getOne: recupera la entidad por su ID")
			void testGetOne() {
				var obtenido = srv.getOne(8).orElseGet(() -> fail("Empty"));
				
				assertAll("Propiedades", 
						() -> assertEquals(8, obtenido.getId()),
						() -> assertEquals("Max", obtenido.getName(), "Name"),
						() -> assertEquals(toDate("1995-09-04"), obtenido.getBirthDate().get(), "BirthDate"));
			}
	
			@Test
			@Order(11) // Apesta
			@DisplayName("add: añade una entidad sin ID")
			void testAdd_withoutKey() {
				var item = new Pet(0, "Toto", "2020-09-07", 1, 1);

				var obtenido = assertDoesNotThrow(() -> srv.add(item));

				assertNotNull(obtenido);
				assertAll("Propiedades", 
						() -> assertEquals(14, obtenido.getId()),
//						() -> assertTrue(obtenido.getId() > 0, "Id"),
						() -> assertEquals("Toto", obtenido.getName(), "Name"),
						() -> assertEquals(toDate("2020-09-07"), obtenido.getBirthDate().get(), "BirthDate"));
			}
			
//			@Test
//			@DisplayName("add: añade una entidad con ID")
//			void testAdd_withKey() {
//				var item = new Pet(22, "Toto", "2020-09-07");
//
//				var obtenido = assertDoesNotThrow(() -> srv.add(item));
//
//				assertNotNull(obtenido);
//				assertAll("Propiedades", 
//						() -> assertEquals(22, obtenido.getId()),
//						() -> assertEquals("Toto", obtenido.getName(), "Name"),
//						() -> assertEquals(toDate("2020-09-07"), obtenido.getBirthDate().get(), "BirthDate"));
//			}
			
			@Test
			@Order(12) // Apesta
			@DisplayName("modify: modifica una entidad existente")
			void testModify() {
				var item = new Pet(14, "Totoo", "2022-09-07", 1, 1);
				
				var obtenido = assertDoesNotThrow(() -> srv.modify(item));
				
				assertAll("Propiedades", 
						() -> assertEquals(14, obtenido.getId()),
						() -> assertEquals("Totoo", obtenido.getName(), "Name"),
						() -> assertEquals(toDate("2022-09-07"), obtenido.getBirthDate().get(), "BirthDate"));
			}

			@Test
			@Order(13) // Apesta
			@DisplayName("deleteById: borra un ID existente")
			void testDeleteById() {
				assertDoesNotThrow(() -> srv.deleteById(14));
			}
			
			@Test
			@Order(14) // Apesta
			@DisplayName("delete: borra una entidad existente")
			void testDelete() {
				var item = new Pet(1, "Toto", "2020-09-07");

				assertDoesNotThrow(() -> srv.delete(item));
			}

			@Test
			@Order(20) // Apesta
			@DisplayName("workflow: flujo de creación, modificación y borrado")
			void testWorkflowCRUD() {
				// add
				var obtenidoAdd = assertDoesNotThrow(() -> srv.add(new Pet(0, "Toto", "2020-09-07", 1, 1)));

				assertNotNull(obtenidoAdd);
				assertAll("Propiedades Add", 
//						() -> assertEquals(1, obtenidoAdd.getId()),
						() -> assertTrue(obtenidoAdd.getId() > 0, "Id"),
						() -> assertEquals("Toto", obtenidoAdd.getName(), "Name"),
						() -> assertEquals(toDate("2020-09-07"), obtenidoAdd.getBirthDate().get(), "BirthDate"));
				
				// update
				var obtenidoModify = assertDoesNotThrow(() -> srv.modify(new Pet(obtenidoAdd.getId(), "TOTO", "2020-09-17", 1, 1)));
				
				assertNotNull(obtenidoModify);
				assertAll("Propiedades Modify", 
						() -> assertEquals(obtenidoAdd.getId(), obtenidoModify.getId(), "Id"),
						() -> assertEquals("TOTO", obtenidoModify.getName(), "Name"),
						() -> assertEquals(toDate("2020-09-17"), obtenidoModify.getBirthDate().get(), "BirthDate"));
				
				// delete
				assertDoesNotThrow(() -> srv.deleteById(obtenidoAdd.getId()));
				
				// verify
				assertTrue(srv.getOne(obtenidoAdd.getId()).isEmpty());
			}

		}
		
		@Nested
		@Order(2) // Apesta
		@DisplayName("Casos inválidos")
		class KO {
			@Test
			@DisplayName("ctor: lanza una IllegalArgumentException al crear una instancia del servicio sin dao")
			void testCtor_PetsService() {
				var ex = assertThrows(IllegalArgumentException.class, () -> new PetsServiceImpl(null));
				assertEquals("dao is null", ex.getMessage());
			}
			
			@Test
			@DisplayName("getOne: recupera un Optional.empty cuando no encuentra el ID")
			void testGetOne() {
				var obtenido = srv.getOne(22);
				
				assertTrue(obtenido.isEmpty());
			}
	
//			@Test
//			@DisplayName("add: lanza una InvalidDataException si no le pasan la entidad a añadir")
//			void testAdd_Null() {
//				var ex = assertThrows(InvalidDataException.class, () -> srv.add(null));
//				assertEquals("No puede ser nulo", ex.getMessage());
//			}
//	
			@Test
			@DisplayName("add: lanza una InvalidDataException si le pasan una entidad con datos inválidos")
			void testAdd_InvalidData() {
				var item = new Pet(1, "  ", "2020-09-07");
				
				var ex = assertThrows(InvalidDataException.class, () -> srv.add(item));
				
				assertEquals("ERRORES: name: no debe estar vacío.", ex.getMessage());
			}
	
			@Test
			@DisplayName("add: lanza una DuplicateKeyException si se intenta añadir una entidad que ya existe")
			void testAdd_Duplicate() {
				var item = new Pet(13, "Toto", "2020-09-07");
				
				assertThrows(DuplicateKeyException.class, () -> srv.add(item));
			}
			
//			@Test
//			@DisplayName("modify: lanza una InvalidDataException si no le pasan la entidad a modificar")
//			void testModify_Null() {
//				var ex = assertThrows(InvalidDataException.class, () -> srv.modify(null));
//				assertEquals("No puede ser nulo", ex.getMessage());
//			}
	
			@Test
			@DisplayName("modify: lanza una InvalidDataException si le pasan una entidad con datos inválidos")
			void testModify_InvalidData() {
				var item = new Pet(1, null, "2020-09-07");
				
				var ex = assertThrows(InvalidDataException.class, () -> srv.modify(item));
				
				assertEquals("ERRORES: name: no debe estar vacío.", ex.getMessage());
			}
	
			@Test
			@DisplayName("modify: lanza una NotFoundException si se intenta modificar una entidad que no existe")
			void testModify_NotFoundException() {
				var item = new Pet(22, "Toto", "2020-09-07");
				
				assertThrows(NotFoundException.class, () -> srv.modify(item));
			}
			
//			@Test
//			@DisplayName("delete: lanza una InvalidDataException si no le pasan la entidad a borrar")
//			void testDelete_Null() {
//				assertThrows(InvalidDataException.class, () -> srv.delete(null));
//				assertEquals("No puede ser nulo", ex.getMessage());
//			}
	
		}
	}
	private static Date toDate(String date) {
		try {
			return (new SimpleDateFormat("yyyy-MM-dd")).parse(date);
		} catch (ParseException e) {
			throw new RuntimeException(e.getMessage(), e);
		}
	}

}
