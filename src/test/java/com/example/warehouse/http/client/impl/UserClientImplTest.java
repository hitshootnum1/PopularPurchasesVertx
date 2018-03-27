package com.example.warehouse.http.client.impl;

import com.example.warehouse.http.client.entity.User;
import io.vertx.reactivex.core.Vertx;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpResponse.response;

public class UserClientImplTest {

    private static Vertx VERTX;

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, 8000);
    private MockServerClient mockServerClient;

    private final String successResponseBody =
            "{\n" +
            "  \"user\": {\n" +
            "    \"username\": \"username\",\n" +
            "    \"email\": \"username@gmail.com\"\n" +
            "  }\n" +
            "}";

    private final String notFoundResponseBody = "{}";

    @BeforeClass
    public static void openVertx() {
        VERTX = Vertx.vertx();
    }

    @AfterClass
    public static void closeVertx() {
        VERTX.close();
    }

    @Test
    public void userByUsernameSuccess() throws Exception {
        mockServerClient
                .when(HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/api/users/username"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(successResponseBody));

        UserClientImpl purchaseClient = new UserClientImpl(VERTX);
        User user = purchaseClient.userByUsername("username").blockingGet();

        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("username");
        assertThat(user.getEmail()).isEqualTo("username@gmail.com");
    }

    @Test(expected = IllegalArgumentException.class)
    public void userByUsernameNotFound() throws Exception {
        mockServerClient
                .when(HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/api/users/username"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(notFoundResponseBody));

        UserClientImpl purchaseClient = new UserClientImpl(VERTX);
        User user = purchaseClient.userByUsername("username").blockingGet();
    }

}