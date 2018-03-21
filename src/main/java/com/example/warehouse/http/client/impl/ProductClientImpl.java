package com.example.warehouse.http.client.impl;

import com.example.warehouse.http.client.ProductClient;
import com.example.warehouse.http.client.entity.Product;
import com.example.warehouse.http.client.entity.ProductInfo;
import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;

import java.util.concurrent.TimeUnit;

import static com.example.warehouse.MainVerticle.API_HOST;
import static com.example.warehouse.MainVerticle.API_PORT;

public class ProductClientImpl implements ProductClient {
    private Vertx vertx;

    public ProductClientImpl(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public Single<Product> productInfo(Integer productId) {
        return WebClient.create(vertx)
                .get(API_PORT, API_HOST,"/api/products/" + productId)
                .rxSend().timeout(2, TimeUnit.SECONDS)
                .map(response -> {
                    ProductInfo productInfo = response.bodyAsJsonObject().mapTo(ProductInfo.class);
                    return productInfo.getProduct();
                });
    }

}
