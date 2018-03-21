package com.example.warehouse.http.client.entity;

import com.example.warehouse.handler.IdentifyAble;

import java.util.List;

public class RecentPurchaseByUser implements IdentifyAble{

    private String username;
    private List<Purchase> purchases;

    public RecentPurchaseByUser() {
    }

    public RecentPurchaseByUser(String username, List<Purchase> purchases) {
        this.username = username;
        this.purchases = purchases;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getIdentity() {
        return identityFromId(username);
    }

    public static String identityFromId(String username) {
        if (username == null) {
            return null;
        }

        return "recent_purchase_user:" + username;
    }

}
