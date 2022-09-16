package com.silence.java9;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TestHttpClient {

	// @Test
	public void simpleGetRequest() throws IOException, InterruptedException {
		var request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:8080/simple/hello"))
				.GET()
				.build();
		var client = HttpClient.newHttpClient();
		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.body());

		request = HttpRequest.newBuilder()
				.uri(URI.create("http://localhost:8080/simple/tom"))
				.GET()
				.build();
		response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.body());
	}
}
