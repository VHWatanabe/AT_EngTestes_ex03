package org.example;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

public class ViaCepClient {
    private final HttpClient client = HttpClient.newHttpClient();

    public String buscarPorCep(String cep) throws IOException, InterruptedException {
        if (!cep.matches("\\d{8}")) {
            throw new IllegalArgumentException("CEP inválido. Deve conter exatamente 8 dígitos numéricos.");
        }

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://viacep.com.br/ws/" + cep + "/json/"))
                .header("User-Agent", "JavaHttpClient")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.body().contains("\"erro\": true")) {
            throw new IllegalArgumentException("CEP não encontrado.");
        }

        return response.body();
    }

    public String buscarPorEndereco(String uf, String cidade, String logradouro) throws IOException, InterruptedException {
        String cidadeEncoded = URLEncoder.encode(cidade, StandardCharsets.UTF_8);
        String logradouroEncoded = URLEncoder.encode(logradouro, StandardCharsets.UTF_8);

        String url = String.format("https://viacep.com.br/ws/%s/%s/%s/json/", uf, cidadeEncoded, logradouroEncoded);

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", "JavaHttpClient")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 404) {
            throw new IllegalArgumentException("Endereço não encontrado (HTTP 404).");
        }

        return response.body();
    }
}
