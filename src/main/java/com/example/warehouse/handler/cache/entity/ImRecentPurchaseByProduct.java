package com.example.warehouse.handler.cache.entity;

import com.example.warehouse.handler.IdentifyAble;
import com.example.warehouse.http.client.entity.Purchase;
import com.example.warehouse.http.client.entity.RecentPurchaseByProduct;

import java.util.List;

public class ImRecentPurchaseByProduct implements IdentifyAble{

    private Integer productId;
    private List<Purchase> purchases;

    public ImRecentPurchaseByProduct(Integer productId, List<Purchase> purchases) {
        this.productId = productId;
        this.purchases = purchases;
    }

    public Integer getProductId() {
        return productId;
    }

    public List<Purchase> getPurchases() {
        return purchases;
    }

    @Override
    public String getIdentity() {
        return RecentPurchaseByProduct.identityFromId(productId);
    }
}
