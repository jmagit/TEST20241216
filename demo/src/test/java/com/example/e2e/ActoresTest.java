package com.example.e2e;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchema;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;


class ActoresTest {

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
		RestAssured.baseURI = RestAssured.DEFAULT_URI;
		RestAssured.port = 8080;		
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
	void testGetAll() {
		get("http://localhost:8080/actores/v1").then()
			.statusCode(200)
			.body("$.size()", equalTo(202));
	}
	@Test
	void testGetOne() {
		get("/actores/v1/1").then()
			.statusCode(200)
			.assertThat().body(matchesJsonSchema("""
					{
						"type": "object",
						"additionalProperties": false,
						"properties": {
							"id": { "type": "integer", "format": "int32" },
							"nombre": { "type": "string" },
							"apellidos": { "type": "string" }
						},
						"required": ["id","nombre"] 
					}					
					"""))
			.body("nombre", is("PENELOPE"));
	}
	@Test
	void testGetOneXML() {
		given().accept(ContentType.XML).log().ifValidationFails().
		when().get("/actores/v1/1").
		then().log().ifValidationFails().statusCode(200).and()
			//.body(hasXPath("//nombre[text()='PENELOPE']"));
			.body("actorDTO.nombre", is("PENELOPE"));
	}
	
	@Test
	void testGetOneResponse() {
//		record Actor(int id, String nombre, String apellidos) {}
		class Actor {
			int id; String nombre; String apellidos;

			public Actor(int id, String nombre, String apellidos) {
				super();
				this.id = id;
				this.nombre = nombre;
				this.apellidos = apellidos;
			}

			public int getId() {
				return id;
			}

			public void setId(int id) {
				this.id = id;
			}

			public String getNombre() {
				return nombre;
			}

			public void setNombre(String nombre) {
				this.nombre = nombre;
			}

			public String getApellidos() {
				return apellidos;
			}

			public void setApellidos(String apellidos) {
				this.apellidos = apellidos;
			}
			
		}
		
//		var actual = get("/actores/v1/1").as(Actor.class);
//		assertEquals(new Actor(1, "PENELOPE", ""), actual);
		var actual = get("/actores/v1/1").andReturn();
		assertEquals("0", actual.getHeader("expires"));
	}
	
	@Test
	void testGetJSON() {
//		var actual = new JsonPath(get("/actores/v1?page=0&size=10").asString());
//		String json = get("/actores/v1?page=0&size=10").asString();
//		var actual = from(json); //with(json);
		var actual = get("/actores/v1?page=0&size=10").jsonPath();
		assertEquals("10", actual.getString("page.size"));
		assertEquals(10, actual.getInt("content.size()"));
		assertEquals(2, actual.getInt("content[1].id"));
	}

}
