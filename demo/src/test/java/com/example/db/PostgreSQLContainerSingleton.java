package com.example.db;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.MountableFile;

public final class PostgreSQLContainerSingleton {
	private static PostgreSQLContainerSingleton instance;
	private PostgreSQLContainer<?> container;

	private PostgreSQLContainerSingleton() {
		container = new PostgreSQLContainer<>("postgres:17-alpine").withInitScript("init-db.sql");
		// .withCopyFileToContainer(MountableFile.forClasspathResource("init-db.sql"),
		// "/docker-entrypoint-initdb.d/");
		container.start();
	}

	public static PostgreSQLContainerSingleton getInstance() {
		if (instance == null)
			instance = new PostgreSQLContainerSingleton();
		return instance;
	}

	public static PostgreSQLContainer<?> getContainer() {
		return getInstance().container;
	}
}