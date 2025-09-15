package com.api_order.config;

import com.api_order.dto.inventory.ResponseProductDTO;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Service
public class InventoryClient {

    private final WebClient webClient;
    private final int maxAttempts = 3;
    private final Duration requestTimeout = Duration.ofSeconds(5);

    public InventoryClient(WebClient.Builder builder) {
        HttpClient httpClient = HttpClient.create()
                .responseTimeout(Duration.ofSeconds(3));

        this.webClient = builder
                .baseUrl("http://localhost:8080/products")
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }

    // Método genérico para qualquer requisição
    public <T> T request(HttpMethod method, String uri, Object body, Class<T> responseType) {
        int attempts = 0;

        while (attempts < maxAttempts) {
            try {
                WebClient.RequestBodySpec requestSpec = webClient.method(method).uri(uri);

                if (body != null) {
                    requestSpec.bodyValue(body);
                }

                return requestSpec
                        .retrieve()
                        .bodyToMono(responseType)
                        .block(requestTimeout);
            } catch (Exception e) {
                attempts++;
                if (attempts >= maxAttempts) {
                    throw new RuntimeException("FAILED to call a API, after " + maxAttempts + " attempts", e);
                }
                try {
                    Thread.sleep((long) Math.pow(2, attempts) * 1000); // backoff exponencial
                } catch (InterruptedException ignored) {}
            }
        }

        return null;
    }

    public ResponseProductDTO getProduct(UUID productId) {
        return request(HttpMethod.GET, "/products/" + productId, null, ResponseProductDTO.class);
    }

    public void updateStock(UUID productId, int stockChange) {
        Map<String, Integer> requestBody = Map.of("stockChange", stockChange);
        request(HttpMethod.PATCH, "/products/" + productId + "/stock", requestBody, Void.class);
    }

    public boolean verifyIfInventoryApiIsUp() {
        try {
            Map response = request(HttpMethod.GET, "/health", null, Map.class);
            return (boolean) response.get("status");
        } catch (Exception e) {
            return false;
        }
    }
}
