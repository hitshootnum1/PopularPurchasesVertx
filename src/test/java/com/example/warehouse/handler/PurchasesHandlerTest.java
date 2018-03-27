package com.example.warehouse.handler;

import com.example.warehouse.dto.PopularPurchaseResponse;
import com.example.warehouse.http.client.ProductClient;
import com.example.warehouse.http.client.PurchaseClient;
import com.example.warehouse.http.client.entity.Product;
import com.example.warehouse.http.client.entity.Purchase;
import com.example.warehouse.http.client.entity.RecentPurchaseByProduct;
import io.reactivex.Single;
import io.vertx.core.json.JsonArray;
import io.vertx.reactivex.core.Vertx;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PurchasesHandlerTest {

    private static Vertx VERTX;

    @Mock private PurchaseClient purchaseClient;
    @Mock private ProductClient productClient;

    @BeforeClass
    public static void openVertx() {
        VERTX = Vertx.vertx();
    }

    @AfterClass
    public static void closeVertx() {
        VERTX.close();
    }

    @Test
    public void getPurchaseByProduct() throws Exception {
        PurchasesHandler handler = new PurchasesHandler(VERTX);
        Whitebox.setInternalState(handler, "purchaseClient", purchaseClient);
        Whitebox.setInternalState(handler, "productClient", productClient);

        RecentPurchaseByProduct expectedPurchase = new RecentPurchaseByProduct(11111, asList(
                new Purchase(1, 11111, "user1", null),
                new Purchase(2, 11111, "user2", null)));

        Product product = new Product(11111, "face", 1, 2);
        Mockito.when(purchaseClient.purchasesByProduct(11111)).thenReturn(Single.just(expectedPurchase));
        Mockito.when(productClient.productInfo(11111)).thenReturn(Single.just(product));

        PopularPurchaseResponse response = handler.getPurchaseByProduct(11111).blockingGet();
        assertThat(response.getRecent()).isEqualTo(asList("user1", "user2"));
        assertThat(response.getFace()).isEqualTo("face");
        assertThat(response.getId()).isEqualTo(11111);
        assertThat(response.getSize()).isEqualTo(1);
    }

    @Test
    public void sortedPurchases() throws Exception {
        PurchasesHandler handler = new PurchasesHandler(VERTX);
        List<PopularPurchaseResponse> input = asList(
                new PopularPurchaseResponse(2, "face 2", 22, asList("user1")),
                new PopularPurchaseResponse(3, "face 3", 33, asList("user1", "user2")),
                new PopularPurchaseResponse(1, "face 1", 11, asList("user1", "user2", "user3")));

        String sorted = handler.sortedPurchases(input.toArray());
        JsonArray arr = new JsonArray(sorted);
        assertThat(arr.getJsonObject(0).getJsonArray("recent").getList())
                .isEqualTo(asList("user1", "user2", "user3"));
        assertThat(arr.getJsonObject(1).getJsonArray("recent").getList())
                .isEqualTo(asList("user1", "user2"));
        assertThat(arr.getJsonObject(2).getJsonArray("recent").getList())
                .isEqualTo(asList("user1"));
    }

}