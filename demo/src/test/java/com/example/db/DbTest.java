package com.example.db;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
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

class DbTest {
	@Container
	GenericContainer postgres = new GenericContainer(DockerImageName.parse("postgres:17-alpine"))
			.withEnv("POSTGRES_USER", "postgres").withEnv("POSTGRES_PASSWORD", "root")
			.withCopyFileToContainer(MountableFile.forClasspathResource("init-db.sql"), "/docker-entrypoint-initdb.d/")
			.withExposedPorts(5432);

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
		System.out.println("jdbc:postgresql://" + postgres.getHost() + ":" + postgres.getFirstMappedPort() + "/postgres");
		connectionProvider = new DBConnectionProvider(
				"jdbc:postgresql://" + postgres.getHost() + ":" + postgres.getFirstMappedPort() + "/postgres",
				"postgres", "root");
	}

	@AfterEach
	void tearDown() throws Exception {
	}

	@Test
	void testConnection() {
		try (Connection conn = this.connectionProvider.getConnection()) {
			var cmd = conn.prepareStatement("select count(*) from vets");
			ResultSet rs = cmd.executeQuery();
			rs.next();
			var actual = rs.getLong(1);
			assertEquals(6, actual);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
