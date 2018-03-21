package com.example.warehouse.handler.cache.entity;

import com.example.warehouse.handler.IdentifyAble;
import com.example.warehouse.http.client.entity.Product;

public final class ImProduct implements IdentifyAble {

    private final Integer id;
    private final String face;
    private final Integer size;
    private final Integer price;

    public ImProduct(Integer id, String face, Integer size, Integer price) {
        this.id = id;
        this.face = face;
        this.size = size;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public String getFace() {
        return face;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getPrice() {
        return price;
    }

    @Override
    public String getIdentity() {
        return Product.identityFromId(id);
    }
}
