package com.example.proxy;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.wiremock.spring.ConfigureWireMock;
import org.wiremock.spring.EnableWireMock;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;

//@WireMockTest(httpPort = 9090)
@EnableWireMock(@ConfigureWireMock(baseUrlProperties = { "localhost" }, port = 9090))
class ActorProxyTest {

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

	@Test
	void testOK() throws Exception {
		stubFor(get(urlEqualTo("/api/actores/1"))
				.withHeader("accept", equalTo("application/json"))
				.willReturn(aResponse()
						.withStatus(200).withFixedDelay(3000)
						.withBody("{ \"id\": 1, \"nombre\":\"John\",\"apellidos\":\"Doe\"}")
				));

		var p = new ActorProxy();

		var actual = p.getActor(1);

		assertTrue(actual.isPresent());
		assertEquals(new ActorDTO(1, "John", "Doe"), actual.get());
	}

	@Test
	void testKO() throws Exception {
		stubFor(get(urlEqualTo("/api/actores/1"))
				.withHeader("accept", equalTo("application/json"))
				.willReturn(aResponse().withStatus(404)));

		var p = new ActorProxy();

		var actual = p.getActor(1);

		assertTrue(actual.isEmpty());
	}

}
