package com.example.warehouse.http.client.entity;

import com.example.warehouse.handler.IdentifyAble;

import java.util.List;

public class RecentPurchaseByProduct implements IdentifyAble {

    private Integer productId;
    private List<Purchase> purchases;

    public RecentPurchaseByProduct() {
    }

    public RecentPurchaseByProduct(Integer productId, List<Purchase> purchases) {
        this.productId = productId;
        this.purchases = purchases;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    public void setPurchases(List<Purchase> purchases) {
        this.purchases = purchases;
    }

    @Override
    public String getIdentity() {
        return identityFromId(productId);
    }

    public static String identityFromId(Integer productId) {
        if (productId == null) {
            return null;
        }

        return "recent_purchase_product:" + productId;
    }

}
