package com.example.warehouse.http.client.impl.cache;

import com.example.warehouse.handler.cache.CacheManagement;
import com.example.warehouse.handler.cache.entity.ImRecentPurchaseByProduct;
import com.example.warehouse.handler.cache.entity.ImRecentPurchaseByUser;
import com.example.warehouse.http.client.entity.Purchase;
import com.example.warehouse.http.client.entity.RecentPurchaseByProduct;
import com.example.warehouse.http.client.entity.RecentPurchaseByUser;
import com.example.warehouse.http.client.impl.PurchaseClientImpl;
import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import java.util.Date;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class PurchaseClientCachedTest {

    @Mock CacheManagement cache;
    @Mock PurchaseClientImpl client;
    @Mock Vertx vertx;
    @Mock Date date;

    @Test
    public void recentPurchasesByUserHaveNotCached() {
        PurchaseClientCached clientCached = new PurchaseClientCached(vertx);
        Whitebox.setInternalState(clientCached, "cache", cache);
        Whitebox.setInternalState(clientCached, "client", client);

        Mockito.when(cache.get("recent_purchase_user:user1", ImRecentPurchaseByUser.class))
                .thenReturn(null);

        RecentPurchaseByUser expectedPurchase = new RecentPurchaseByUser("user1",
                asList(new Purchase(1, 11, "user1", date),
                        new Purchase(2, 22, "user1", date)));

        Mockito.when(client.recentPurchasesByUser("user1")).thenReturn(Single.just(expectedPurchase));

        RecentPurchaseByUser purchase = clientCached.recentPurchasesByUser("user1").blockingGet();
        assertThat(purchase).isNotNull();
        assertThat(purchase.getUsername()).isEqualTo("user1");
        assertThat(purchase.getPurchases()).hasSize(2);

        Purchase p1 = purchase.getPurchases().get(0);
        assertThat(p1.getId()).isEqualTo(1);
        assertThat(p1.getDate()).isEqualTo(date);
        assertThat(p1.getUsername()).isEqualTo("user1");
        assertThat(p1.getProductId()).isEqualTo(11);

        Purchase p2 = purchase.getPurchases().get(1);
        assertThat(p2.getId()).isEqualTo(2);
        assertThat(p2.getDate()).isEqualTo(date);
        assertThat(p2.getUsername()).isEqualTo("user1");
        assertThat(p2.getProductId()).isEqualTo(22);
    }

    @Test
    public void recentPurchasesByUserHaveBeenCached() {
        PurchaseClientCached clientCached = new PurchaseClientCached(vertx);
        Whitebox.setInternalState(clientCached, "cache", cache);

        Mockito.when(cache.get("recent_purchase_user:user1", ImRecentPurchaseByUser.class))
                .thenReturn(new ImRecentPurchaseByUser("user1",
                        asList(new Purchase(1, 11, "user1", date),
                                new Purchase(2, 22, "user1", date))));

        RecentPurchaseByUser purchase = clientCached.recentPurchasesByUser("user1").blockingGet();
        assertThat(purchase).isNotNull();
        assertThat(purchase.getUsername()).isEqualTo("user1");
        assertThat(purchase.getPurchases()).hasSize(2);

        Purchase p1 = purchase.getPurchases().get(0);
        assertThat(p1.getId()).isEqualTo(1);
        assertThat(p1.getDate()).isEqualTo(date);
        assertThat(p1.getUsername()).isEqualTo("user1");
        assertThat(p1.getProductId()).isEqualTo(11);

        Purchase p2 = purchase.getPurchases().get(1);
        assertThat(p2.getId()).isEqualTo(2);
        assertThat(p2.getDate()).isEqualTo(date);
        assertThat(p2.getUsername()).isEqualTo("user1");
        assertThat(p2.getProductId()).isEqualTo(22);
    }

    @Test
    public void recentPurchasesByProductHaveNotCached() {
        PurchaseClientCached clientCached = new PurchaseClientCached(vertx);
        Whitebox.setInternalState(clientCached, "cache", cache);
        Whitebox.setInternalState(clientCached, "client", client);

        Mockito.when(cache.get("recent_purchase_product:99999", ImRecentPurchaseByProduct.class))
                .thenReturn(null);

        RecentPurchaseByProduct expectedPurchase = new RecentPurchaseByProduct(99999,
                asList(new Purchase(1, 11, "user1", date),
                        new Purchase(2, 22, "user1", date)));

        Mockito.when(client.purchasesByProduct(99999)).thenReturn(Single.just(expectedPurchase));

        RecentPurchaseByProduct purchase = clientCached.purchasesByProduct(99999).blockingGet();
        assertThat(purchase).isNotNull();
        assertThat(purchase.getProductId()).isEqualTo(99999);
        assertThat(purchase.getPurchases()).hasSize(2);

        Purchase p1 = purchase.getPurchases().get(0);
        assertThat(p1.getId()).isEqualTo(1);
        assertThat(p1.getDate()).isEqualTo(date);
        assertThat(p1.getUsername()).isEqualTo("user1");
        assertThat(p1.getProductId()).isEqualTo(11);

        Purchase p2 = purchase.getPurchases().get(1);
        assertThat(p2.getId()).isEqualTo(2);
        assertThat(p2.getDate()).isEqualTo(date);
        assertThat(p2.getUsername()).isEqualTo("user1");
        assertThat(p2.getProductId()).isEqualTo(22);
    }

    @Test
    public void recentPurchasesByProductHaveBeenCached() {
        PurchaseClientCached clientCached = new PurchaseClientCached(vertx);
        Whitebox.setInternalState(clientCached, "cache", cache);

        Mockito.when(cache.get("recent_purchase_product:99999", ImRecentPurchaseByProduct.class))
                .thenReturn(new ImRecentPurchaseByProduct(99999,
                        asList(new Purchase(1, 11, "user1", date),
                                new Purchase(2, 22, "user1", date))));


        RecentPurchaseByProduct purchase = clientCached.purchasesByProduct(99999).blockingGet();
        assertThat(purchase).isNotNull();
        assertThat(purchase.getProductId()).isEqualTo(99999);
        assertThat(purchase.getPurchases()).hasSize(2);

        Purchase p1 = purchase.getPurchases().get(0);
        assertThat(p1.getId()).isEqualTo(1);
        assertThat(p1.getDate()).isEqualTo(date);
        assertThat(p1.getUsername()).isEqualTo("user1");
        assertThat(p1.getProductId()).isEqualTo(11);

        Purchase p2 = purchase.getPurchases().get(1);
        assertThat(p2.getId()).isEqualTo(2);
        assertThat(p2.getDate()).isEqualTo(date);
        assertThat(p2.getUsername()).isEqualTo("user1");
        assertThat(p2.getProductId()).isEqualTo(22);
    }
}