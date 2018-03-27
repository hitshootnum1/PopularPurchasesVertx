package com.example.warehouse.http.client.impl;

import com.example.warehouse.http.client.entity.Product;
import io.vertx.reactivex.core.Vertx;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

public class ProductClientImplTest {

    private static Vertx VERTX;

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, 8000);
    private MockServerClient mockServerClient;

    @BeforeClass
    public static void openVertx() {
        VERTX = Vertx.vertx();
    }

    @AfterClass
    public static void closeVertx() {
        VERTX.close();
    }

    @Test
    public void productInfoSuccess() throws Exception {
        String responseBody = "" +
                "{\n" +
                "  \"product\": {\n" +
                "    \"id\": 599228,\n" +
                "    \"face\": \"ヽ༼ʘ̚ل͜ʘ̚༽ﾉ\",\n" +
                "    \"price\": 297,\n" +
                "    \"size\": 25\n" +
                "  }\n" +
                "}";

        mockServerClient
                .when(HttpRequest.request()
                                .withMethod("GET")
                                .withPath("/api/products/11111"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBody));

        ProductClientImpl productClient = new ProductClientImpl(VERTX);
        Product product = productClient.productInfo(11111).blockingGet();

        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(599228);
        assertThat(product.getFace()).isEqualTo("ヽ༼ʘ̚ل͜ʘ̚༽ﾉ");
        assertThat(product.getPrice()).isEqualTo(297);
        assertThat(product.getSize()).isEqualTo(25);
    }

    @Test
    public void productInfoNotFound() throws Exception {
        String responseBody = "{}";

        new MockServerClient("localhost", 8000)
                .when(
                        request()
                                .withMethod("GET")
                                .withPath("/api/products/11111"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(responseBody));

        ProductClientImpl productClient = new ProductClientImpl(VERTX);
        Product product = productClient.productInfo(11111).blockingGet();
        assertThat(product).isNotNull();
        assertThat(product.getId()).isNull();
        assertThat(product.getSize()).isNull();
        assertThat(product.getPrice()).isNull();
        assertThat(product.getFace()).isNull();
    }
}