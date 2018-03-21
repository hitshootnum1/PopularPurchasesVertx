package com.example.warehouse.handler.cache.mapper;

import com.example.warehouse.handler.cache.entity.ImRecentPurchaseByUser;
import com.example.warehouse.http.client.entity.RecentPurchaseByUser;

public class RecentPurchaseMapper {

    public static ImRecentPurchaseByUser toImmutable(RecentPurchaseByUser recentPurchaseByUser) {
        return new ImRecentPurchaseByUser(recentPurchaseByUser.getUsername(), recentPurchaseByUser.getPurchases());
    }

    public static RecentPurchaseByUser toMutable(ImRecentPurchaseByUser recentPurchase) {
        return new RecentPurchaseByUser(recentPurchase.getUsername(), recentPurchase.getPurchases());
    }
}
