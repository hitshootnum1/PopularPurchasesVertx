package com.example.warehouse;

import com.example.warehouse.handler.cache.CacheManagement;
import io.restassured.response.ResponseBodyExtractionOptions;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.junit.*;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.junit.MockServerRule;
import org.mockserver.model.HttpRequest;

import static io.restassured.RestAssured.when;
import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockserver.model.HttpResponse.response;

public class PurchaseRoutingTest {

    @Rule
    public MockServerRule mockServerRule = new MockServerRule(this, 8000);
    private MockServerClient mockServerClient;

    private final String baseUrl = "http://localhost:8080";

    private final String successUserBody =
                    "{\n" +
                    "  \"user\": {\n" +
                    "    \"username\": \"user1\",\n" +
                    "    \"email\": \"user1@gmail.com\"\n" +
                    "  }\n" +
                    "}";

    private final String successRecentPurchaseByUserBody =
                    "{\n" +
                    "  \"purchases\": [\n" +
                    "    {\n" +
                    "      \"id\": 342190,\n" +
                    "      \"username\": \"user1\",\n" +
                    "      \"productId\": 99999,\n" +
                    "      \"date\": \"2018-03-26T03:42:09.088Z\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 487271,\n" +
                    "      \"username\": \"user1\",\n" +
                    "      \"productId\": 11111,\n" +
                    "      \"date\": \"2018-03-19T11:10:44.090Z\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

    private final String successRecentPurchaseByProduct11111Body =
                    "{\n" +
                    "  \"purchases\": [\n" +
                    "    {\n" +
                    "      \"id\": 487270,\n" +
                    "      \"username\": \"user0\",\n" +
                    "      \"productId\": 11111,\n" +
                    "      \"date\": \"2018-03-26T03:42:09.088Z\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 487271,\n" +
                    "      \"username\": \"user1\",\n" +
                    "      \"productId\": 11111,\n" +
                    "      \"date\": \"2018-03-19T11:10:44.090Z\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

    private final String successRecentPurchaseByProduct9999Body =
                    "{\n" +
                    "  \"purchases\": [\n" +
                    "    {\n" +
                    "      \"id\": 342190,\n" +
                    "      \"username\": \"user1\",\n" +
                    "      \"productId\": 99999,\n" +
                    "      \"date\": \"2018-03-26T03:42:09.088Z\"\n" +
                    "    },\n" +
                    "    {\n" +
                    "      \"id\": 342191,\n" +
                    "      \"username\": \"user2\",\n" +
                    "      \"productId\": 99999,\n" +
                    "      \"date\": \"2018-03-19T11:10:44.090Z\"\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";
    private final String emptyPurchaseBody = "{\"purchases\": []}";

    private final String successProductId11111 =
                    "{\n" +
                    "  \"product\": {\n" +
                    "    \"id\": 11111,\n" +
                    "    \"face\": \"face 11111\",\n" +
                    "    \"price\": 1,\n" +
                    "    \"size\": 111\n" +
                    "  }\n" +
                    "}";

    private final String successProductId9999 =
                    "{\n" +
                    "  \"product\": {\n" +
                    "    \"id\": 99999,\n" +
                    "    \"face\": \"face 99999\",\n" +
                    "    \"price\": 99,\n" +
                    "    \"size\": 999\n" +
                    "  }\n" +
                    "}";

    @Before
    public void setup() {
        CacheManagement.getInstance().invalidCache();
    }

    @BeforeClass
    public static void startServer() {
        MainVerticle.deploy(null);
    }

    @AfterClass
    public static void stopServer() {
        MainVerticle.unDeploy();
    }

    @Test
    public void returnErrorBodyWhenUserNotFound() throws Exception {
        mockExternalAPI("/api/users/user1", "{}");

        String error = callUserPurchaseAPI(404).path("error");

        assertThat(error).isEqualTo("User with username of 'user1' was not found");
    }

    @Test
    public void returnErrorBodyWhenUserFound() throws Exception {
        mockExternalAPI("/api/users/user1", successUserBody);
        mockExternalAPI("/api/purchases/by_user/user1", successRecentPurchaseByUserBody);
        mockExternalAPI("/api/purchases/by_product/11111", successRecentPurchaseByProduct11111Body);
        mockExternalAPI("/api/purchases/by_product/99999", successRecentPurchaseByProduct9999Body);
        mockExternalAPI("/api/products/99999", successProductId9999);
        mockExternalAPI("/api/products/11111", successProductId11111);

        String responseBody = callUserPurchaseAPI(200).asString();
        JsonArray arr = new JsonArray(responseBody);
        assertThat(arr).hasSize(2);
        JsonObject p1 = arr.getJsonObject(0);
        assertThat(p1.getInteger("id")).isEqualTo(99999);
        assertThat(p1.getString("face")).isEqualTo("face 99999");
        assertThat(p1.getInteger("size")).isEqualTo(999);
        assertThat(p1.getJsonArray("recent").getList()).isEqualTo(asList("user1", "user2"));

        JsonObject p2 = arr.getJsonObject(1);
        assertThat(p2.getInteger("id")).isEqualTo(11111);
        assertThat(p2.getString("face")).isEqualTo("face 11111");
        assertThat(p2.getInteger("size")).isEqualTo(111);
        assertThat(p2.getJsonArray("recent").getList()).isEqualTo(asList("user0", "user1"));
    }


    @Test
    public void returnErrorBodyWhenUserFoundButOneProductNotFound() throws Exception {
        mockExternalAPI("/api/users/user1", successUserBody);
        mockExternalAPI("/api/purchases/by_user/user1", successRecentPurchaseByUserBody);
        mockExternalAPI("/api/purchases/by_product/11111", successRecentPurchaseByProduct11111Body);
        mockExternalAPI("/api/purchases/by_product/99999", emptyPurchaseBody);
        mockExternalAPI("/api/products/99999", "{}");
        mockExternalAPI("/api/products/11111", successProductId11111);

        String responseBody = callUserPurchaseAPI(200).asString();
        JsonArray arr = new JsonArray(responseBody);
        assertThat(arr).hasSize(2);
        JsonObject p1 = arr.getJsonObject(0);
        assertThat(p1.getInteger("id")).isEqualTo(11111);
        assertThat(p1.getString("face")).isEqualTo("face 11111");
        assertThat(p1.getInteger("size")).isEqualTo(111);
        assertThat(p1.getJsonArray("recent").getList()).isEqualTo(asList("user0","user1"));

        JsonObject p2 = arr.getJsonObject(1);
        assertThat(p2.getInteger("id")).isNull();
        assertThat(p2.getString("face")).isNull();
        assertThat(p2.getInteger("size")).isNull();
        assertThat(p2.getJsonArray("recent")).isEmpty();
    }

    private ResponseBodyExtractionOptions callUserPurchaseAPI(int statusCode) {
        return when()
                .get(baseUrl + "/api/recent_purchases/user1")
                .then()
                .statusCode(statusCode)
                .contentType("application/json")
                .extract()
                .body();
    }

    private void mockExternalAPI(String path, String responseBody) {
        mockServerClient
                .when(HttpRequest
                        .request()
                        .withMethod("GET")
                        .withPath(path))
                .respond(
                        response()
                            .withStatusCode(200)
                            .withHeader("Content-Type", "application/json")
                            .withBody(responseBody));
    }
}
