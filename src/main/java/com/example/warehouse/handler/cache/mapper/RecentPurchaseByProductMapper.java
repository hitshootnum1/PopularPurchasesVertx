package com.example.warehouse.handler.cache.mapper;

import com.example.warehouse.handler.cache.entity.ImRecentPurchaseByProduct;
import com.example.warehouse.http.client.entity.RecentPurchaseByProduct;

public class RecentPurchaseByProductMapper {

    public static ImRecentPurchaseByProduct toImmutable(RecentPurchaseByProduct RecentPurchaseByProduct) {
        return new ImRecentPurchaseByProduct(RecentPurchaseByProduct.getProductId(), RecentPurchaseByProduct.getPurchases());
    }

    public static RecentPurchaseByProduct toMutable(ImRecentPurchaseByProduct recentPurchase) {
        return new RecentPurchaseByProduct(recentPurchase.getProductId(), recentPurchase.getPurchases());
    }
}
