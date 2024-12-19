package com.example.db;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.testcontainers.containers.PostgreSQLContainer;

@TestMethodOrder(OrderAnnotation.class)
class PostgresqlTest {

	//@Container
	PostgreSQLContainer<?> postgres = 
//			PostgreSQLContainerSingleton.getContainer()
			new PostgreSQLContainer<>("postgres:17-alpine")
				.withInitScript("init-db.sql")
//				.withCopyFileToContainer(MountableFile.forClasspathResource("init-db.sql"), "/docker-entrypoint-initdb.d/")
			;
	DBConnectionProvider connectionProvider;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@AfterAll
	static void tearDownAfterClass() throws Exception {
	}

	@BeforeEach
	void setUp() throws Exception {
		postgres.start();
		connectionProvider = new DBConnectionProvider(postgres.getJdbcUrl(), postgres.getUsername(),
				postgres.getPassword());
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	@Order(3)
	void testConnection() {
	    try (Connection conn = this.connectionProvider.getConnection()) {
	        var cmd = conn.prepareStatement("select count(*) from pets");
	        ResultSet rs = cmd.executeQuery();
	        rs.next();
	        var actual = rs.getLong(1);
	        assertEquals(13, actual);
	      } catch (SQLException e) {
	        throw new RuntimeException(e);
	      }
	}

	@Test
	@Order(2)
	void testDelete() {
		try (Connection conn = this.connectionProvider.getConnection()) {
			var actual = conn.createStatement().executeUpdate("delete from pets where id < 7");
			assertEquals(6, actual);
			ResultSet rs = conn.createStatement().executeQuery("select count(*) from pets");
			rs.next();
			assertEquals(7, rs.getLong(1));
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
