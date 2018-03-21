package com.example.warehouse.http.client.impl.cache;

import com.example.warehouse.handler.cache.CacheManagement;
import com.example.warehouse.handler.cache.entity.ImRecentPurchaseByProduct;
import com.example.warehouse.handler.cache.entity.ImRecentPurchaseByUser;
import com.example.warehouse.handler.cache.mapper.RecentPurchaseByProductMapper;
import com.example.warehouse.handler.cache.mapper.RecentPurchaseMapper;
import com.example.warehouse.http.client.PurchaseClient;
import com.example.warehouse.http.client.entity.RecentPurchaseByProduct;
import com.example.warehouse.http.client.entity.RecentPurchaseByUser;
import com.example.warehouse.http.client.impl.PurchaseClientImpl;
import io.reactivex.Single;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;

public class PurchaseClientCached implements PurchaseClient {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CacheManagement cache = CacheManagement.getInstance();

    private PurchaseClientImpl client;

    public PurchaseClientCached(Vertx vertx) {
        client = new PurchaseClientImpl(vertx);
    }

    @Override
    public Single<RecentPurchaseByUser> recentPurchasesByUser(String username) {
        ImRecentPurchaseByUser recentPurchase = cache.get(
                RecentPurchaseByUser.identityFromId(username),
                ImRecentPurchaseByUser.class);

        if (recentPurchase != null) {
            logger.info("Recent purchases from user " + username + " are retrieved from cache");
            return Single.just(RecentPurchaseMapper.toMutable(recentPurchase));
        } else {
            logger.info("Recent purchases from user " + username + " are not cached");
            return client.recentPurchasesByUser(username)
                    .doOnSuccess(recent -> {
                        cache.putIfNotExists(RecentPurchaseMapper.toImmutable(recent));
                    });
        }
    }

    @Override
    public Single<RecentPurchaseByProduct> purchasesByProduct(Integer productId) {
        ImRecentPurchaseByProduct recentPurchase = cache.get(
                RecentPurchaseByProduct.identityFromId(productId),
                ImRecentPurchaseByProduct.class);

        if (recentPurchase != null) {
            return Single.just(RecentPurchaseByProductMapper.toMutable(recentPurchase));
        } else {
            return client.purchasesByProduct(productId)
                    .doOnSuccess(recent -> {
                        cache.putIfNotExists(RecentPurchaseByProductMapper.toImmutable(recent));
                    });
        }
    }
}
