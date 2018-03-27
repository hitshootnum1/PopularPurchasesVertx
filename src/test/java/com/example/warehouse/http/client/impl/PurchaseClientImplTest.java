package com.example.warehouse.http.client.impl;

import com.example.warehouse.http.client.entity.Purchase;
import com.example.warehouse.http.client.entity.RecentPurchaseByProduct;
import com.example.warehouse.http.client.entity.RecentPurchaseByUser;
import io.vertx.reactivex.core.Vertx;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpResponse.response;

public class PurchaseClientImplTest {

    private static Vertx VERTX;

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, 8000);
    private MockServerClient mockServerClient;

    private final String successResponseBody =
            "{\n" +
                    "  \"purchases\": [\n" +
                    "    {\n" +
                    "      \"id\": 342190,\n" +
                    "      \"username\": \"userPurchase\",\n" +
                    "      \"productId\": 599228,\n" +
                    "      \"date\": \"2018-03-26T03:42:09.088Z\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 487271,\n" +
                    "      \"username\": \"userPurchase\",\n" +
                    "      \"productId\": 864403,\n" +
                    "      \"date\": \"2018-03-19T11:10:44.090Z\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

    private final String notFoundResponseBody = "{\"purchases\": []}";

    @BeforeClass
    public static void openVertx() {
        VERTX = Vertx.vertx();
    }

    @AfterClass
    public static void closeVertx() {
        VERTX.close();
    }

    @Test
    public void recentPurchasesByUserSuccess() throws Exception {
        mockServerClient
                .when(HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/api/purchases/by_user/userPurchase"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(successResponseBody));

        PurchaseClientImpl purchaseClient = new PurchaseClientImpl(VERTX);
        RecentPurchaseByUser recents = purchaseClient.recentPurchasesByUser("userPurchase").blockingGet();

        assertThat(recents).isNotNull();
        assertThat(recents.getPurchases()).hasSize(2);
        List<Purchase> purchases = recents.getPurchases();
        Purchase p1 = purchases.get(0);
        assertThat(p1).isNotNull();
        assertThat(p1.getProductId()).isEqualTo(599228);
        assertThat(p1.getUsername()).isEqualTo("userPurchase");
        assertThat(p1.getDate()).isEqualTo(Date.from(ZonedDateTime.parse("2018-03-26T03:42:09.088Z").toInstant()));
        assertThat(p1.getId()).isEqualTo(342190);

        Purchase p2 = purchases.get(1);
        assertThat(p2).isNotNull();
        assertThat(p2.getProductId()).isEqualTo(864403);
        assertThat(p2.getUsername()).isEqualTo("userPurchase");
        assertThat(p2.getDate()).isEqualTo(Date.from(ZonedDateTime.parse("2018-03-19T11:10:44.090Z").toInstant()));
        assertThat(p2.getId()).isEqualTo(487271);
    }

    @Test
    public void recentPurchasesByUserNotFound() throws Exception {
        mockServerClient
                .when(HttpRequest.request()
                        .withMethod("GET")
                        .withPath("/api/purchases/by_user/userPurchase"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(notFoundResponseBody));

        PurchaseClientImpl purchaseClient = new PurchaseClientImpl(VERTX);
        RecentPurchaseByUser recents = purchaseClient.recentPurchasesByUser("userPurchase").blockingGet();

        assertThat(recents).isNotNull();
        assertThat(recents.getPurchases()).isEmpty();
    }

    @Test
    public void purchasesByProductSuccess() throws Exception {
        mockServerClient
                .when(HttpRequest
                    .request()
                        .withMethod("GET")
                        .withPath("/api/purchases/by_product/99999"))
                .respond(
                    response()
                        .withStatusCode(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(successResponseBody));

        PurchaseClientImpl purchaseClient = new PurchaseClientImpl(VERTX);
        RecentPurchaseByProduct recents = purchaseClient.purchasesByProduct(99999).blockingGet();

        assertThat(recents).isNotNull();
        assertThat(recents.getPurchases()).hasSize(2);
        List<Purchase> purchases = recents.getPurchases();
        Purchase p1 = purchases.get(0);
        assertThat(p1).isNotNull();
        assertThat(p1.getProductId()).isEqualTo(599228);
        assertThat(p1.getUsername()).isEqualTo("userPurchase");
        assertThat(p1.getDate()).isEqualTo(Date.from(ZonedDateTime.parse("2018-03-26T03:42:09.088Z").toInstant()));
        assertThat(p1.getId()).isEqualTo(342190);

        Purchase p2 = purchases.get(1);
        assertThat(p2).isNotNull();
        assertThat(p2.getProductId()).isEqualTo(864403);
        assertThat(p2.getUsername()).isEqualTo("userPurchase");
        assertThat(p2.getDate()).isEqualTo(Date.from(ZonedDateTime.parse("2018-03-19T11:10:44.090Z").toInstant()));
        assertThat(p2.getId()).isEqualTo(487271);
    }

    @Test
    public void purchasesByProductNotFound() throws Exception {
        mockServerClient
                .when(HttpRequest
                        .request()
                        .withMethod("GET")
                        .withPath("/api/purchases/by_product/99999"))
                .respond(
                        response()
                                .withStatusCode(200)
                                .withHeader("Content-Type", "application/json")
                                .withBody(notFoundResponseBody));

        PurchaseClientImpl purchaseClient = new PurchaseClientImpl(VERTX);
        RecentPurchaseByProduct recents = purchaseClient.purchasesByProduct(99999).blockingGet();

        assertThat(recents).isNotNull();
        assertThat(recents.getPurchases()).isEmpty();
    }
}