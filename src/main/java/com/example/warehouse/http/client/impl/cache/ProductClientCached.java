package com.example.warehouse.http.client.impl.cache;

import com.example.warehouse.handler.cache.CacheManagement;
import com.example.warehouse.handler.cache.entity.ImProduct;
import com.example.warehouse.handler.cache.mapper.ProductMapper;
import com.example.warehouse.http.client.ProductClient;
import com.example.warehouse.http.client.entity.Product;
import com.example.warehouse.http.client.impl.ProductClientImpl;
import io.reactivex.Single;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;

public class ProductClientCached implements ProductClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final CacheManagement cache = CacheManagement.getInstance();
    private ProductClientImpl client;

    public ProductClientCached(Vertx vertx) {
        client = new ProductClientImpl(vertx);
    }

    @Override
    public Single<Product> productInfo(Integer productId) {
        ImProduct cached = cache.get(Product.identityFromId(productId), ImProduct.class);
        if (cached != null) {
            Product product = ProductMapper.toMutable(cached);
            return Single.just(product);
        } else {
            return client
                    .productInfo(productId)
                    .doOnSuccess(product -> cache.putIfNotExists(ProductMapper.toImmutable(product)));
        }
    }
}
