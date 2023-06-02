package com.rest.basic.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SpotifyApi{
    public static String runSpotifyApiClient(String base_url,String bearer_token,String artistId) throws IOException, InterruptedException {
        String artistProfileEndpoint = base_url + "/artists/" + artistId;
        HttpResponse<String> response = sendGetRequest(artistProfileEndpoint, bearer_token);
        return response.body();
    }
    private static HttpResponse<String> sendGetRequest(String url, String bearerToken) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(url))
                .setHeader("Authorization", "Bearer " + bearerToken)
                .setHeader("Content-Type", "application/json")
                .build();

        HttpClient client = HttpClient.newHttpClient();
        return client.send(request, HttpResponse.BodyHandlers.ofString());
    }

}
