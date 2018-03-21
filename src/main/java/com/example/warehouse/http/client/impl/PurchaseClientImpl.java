package com.example.warehouse.http.client.impl;

import com.example.warehouse.http.client.PurchaseClient;
import com.example.warehouse.http.client.entity.RecentPurchaseByProduct;
import com.example.warehouse.http.client.entity.RecentPurchaseByUser;
import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.warehouse.MainVerticle.API_HOST;
import static com.example.warehouse.MainVerticle.API_PORT;

public class PurchaseClientImpl implements PurchaseClient {

    private Vertx vertx;

    public PurchaseClientImpl(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public Single<RecentPurchaseByUser> recentPurchasesByUser(String username) {
        return WebClient.create(vertx)
                .get(API_PORT, API_HOST,"/api/purchases/by_user/" + username)
                .addQueryParam("limit", "5")
                .rxSend().timeout(2, TimeUnit.SECONDS)
                .map(response -> {
                    RecentPurchaseByUser recentPurchaseByUser = response.bodyAsJsonObject().mapTo(RecentPurchaseByUser.class);
                    recentPurchaseByUser.setUsername(username);
                    return recentPurchaseByUser;
                });

    }

    @Override
    public Single<RecentPurchaseByProduct> purchasesByProduct(Integer productId) {
        return WebClient.create(vertx)
                .get(API_PORT, API_HOST, "/api/purchases/by_product/" + productId)
                .addQueryParam("limit", String.valueOf(Integer.MAX_VALUE))
                .rxSend().timeout(2, TimeUnit.SECONDS)
                .map(response -> {
                    RecentPurchaseByProduct recentPurchase = response.bodyAsJsonObject().mapTo(RecentPurchaseByProduct.class);
                    recentPurchase.setProductId(productId);
                    return recentPurchase;
                });
    }
}
