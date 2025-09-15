package com.api_order.config;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class InventoryClientTest {

    private MockWebServer mockWebServer;
    private InventoryClient inventoryClient;

    @BeforeEach
    void setUp() throws IOException {
        mockWebServer = new MockWebServer();
        mockWebServer.start();

        // Cria o InventoryClient com a URL do MockWebServer
        WebClient.Builder builder = WebClient.builder();
        inventoryClient = new InventoryClient(builder);
    }

    @AfterEach
    void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void verifyIfInventoryApiIsUp_shouldReturnTrueWhenHealthEndpointReturnsTrue() {
        // Configura resposta mockada com sucesso
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("true")
                .addHeader("Content-Type", "application/json"));

        Boolean result = inventoryClient.verifyIfInventoryApiIsUp();

        assertTrue(result);
    }

    @Test
    void verifyIfInventoryApiIsUp_shouldReturnFalseWhenHealthEndpointReturnsFalse() {
        // Configura resposta com false
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("false")
                .addHeader("Content-Type", "application/json"));

        Boolean result = inventoryClient.verifyIfInventoryApiIsUp();

        assertFalse(result);
    }

    @Test
    void verifyIfInventoryApiIsUp_shouldReturnFalseWhenHealthEndpointFails() {
        // Configura erro 500
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(500)
                .setBody("Internal Server Error"));

        Boolean result = inventoryClient.verifyIfInventoryApiIsUp();

        assertFalse(result);
    }

    @Test
    void verifyIfInventoryApiIsUp_shouldReturnFalseWhenTimeout() {
        // Configura timeout (demora 10 segundos para responder)
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("true")
                .setBodyDelay(TimeUnit.SECONDS.toMillis(6), null)); // 6000ms

        Boolean result = inventoryClient.verifyIfInventoryApiIsUp();

        assertFalse(result);
    }

    @Test
    void verifyIfInventoryApiIsUp_shouldRetryThreeTimes() {
        // Configura 2 falhas seguidas e depois sucesso
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        mockWebServer.enqueue(new MockResponse().setResponseCode(500));
        mockWebServer.enqueue(new MockResponse()
                .setResponseCode(200)
                .setBody("true"));

        Boolean result = inventoryClient.verifyIfInventoryApiIsUp();

        assertTrue(result);
        // Verifica que houve 3 requisições (2 falhas + 1 sucesso)
        assertEquals(3, mockWebServer.getRequestCount());
    }
}