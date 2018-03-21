package com.example.warehouse.handler.cache.entity;

import com.example.warehouse.handler.IdentifyAble;
import com.example.warehouse.http.client.entity.Purchase;

import java.util.List;

import static com.example.warehouse.http.client.entity.RecentPurchaseByUser.identityFromId;

public class ImRecentPurchaseByUser implements IdentifyAble {

    private String username;
    private List<Purchase> purchases;

    public ImRecentPurchaseByUser(String username, List<Purchase> purchases) {
        this.username = username;
        this.purchases = purchases;
    }

    public String getUsername() {
        return username;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    @Override
    public String getIdentity() {
        return identityFromId(username);
    }
}
