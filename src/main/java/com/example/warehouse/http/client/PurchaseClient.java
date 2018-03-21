package com.example.warehouse.http.client;

import com.example.warehouse.http.client.entity.RecentPurchaseByProduct;
import com.example.warehouse.http.client.entity.RecentPurchaseByUser;
import io.reactivex.Single;

import java.util.List;

public interface PurchaseClient {
    Single<RecentPurchaseByUser> recentPurchasesByUser(String username);

    Single<RecentPurchaseByProduct> purchasesByProduct(Integer productId);
}
