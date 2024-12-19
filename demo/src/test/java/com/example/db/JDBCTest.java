package com.example.db;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import org.testcontainers.utility.MountableFile;

class JDBCTest {
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

	Connection getConnection() {
		try {
			return DriverManager.getConnection("jdbc:tc:postgresql:17-alpine:///databasename?TC_INITSCRIPT=init-db.sql");
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void testConnection() {
		try (Connection conn = this.getConnection()) {
			var cmd = conn.prepareStatement("select count(*) from vets");
			ResultSet rs = cmd.executeQuery();
			rs.next();
			var actual = rs.getLong(1);
			assertEquals(6, actual);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	void testDelete() {
		try (Connection conn = this.getConnection()) {
			var actual = conn.createStatement().executeUpdate("delete from vets");
			assertEquals(6, actual);
//			var cmd = conn.prepareStatement("select count(*) from vets");
//			ResultSet rs = cmd.executeQuery();
//			rs.next();
//			var actual = rs.getLong(1);
//			assertEquals(6, actual);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
