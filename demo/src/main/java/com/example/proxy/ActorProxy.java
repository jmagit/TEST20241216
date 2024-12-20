package com.example.proxy;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.Optional;

import com.fasterxml.jackson.databind.ObjectMapper;

public class ActorProxy {
	private HttpClient proxy;
	private ObjectMapper mapper;
	
	public ActorProxy() {
		proxy = HttpClient.newHttpClient();
		mapper = new ObjectMapper();
	}

	public Optional<ActorDTO> getActor(int id) throws URISyntaxException, IOException, InterruptedException {
		var request = HttpRequest.newBuilder(new URI("http://localhost:9090/api/actores/" + id))
				.header("accept", "application/json")
				.GET()
				.build();
		HttpResponse<String> response = proxy.send(request, BodyHandlers.ofString());
		if(response.statusCode() != 200)
			return Optional.empty();
		return Optional.of(mapper.readValue(response.body(), ActorDTO.class));
	}
}
