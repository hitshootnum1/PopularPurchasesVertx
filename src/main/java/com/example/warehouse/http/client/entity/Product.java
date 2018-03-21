package com.example.warehouse.http.client.entity;

import com.example.warehouse.handler.IdentifyAble;

public class Product implements IdentifyAble {

    private Integer id;
    private String face;
    private Integer size;
    private Integer price;

    public Product() {
    }

    public Product(Integer id, String face, Integer size, Integer price) {
        this.id = id;
        this.face = face;
        this.size = size;
        this.price = price;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFace() {
        return face;
    }

    public void setFace(String face) {
        this.face = face;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    @Override
    public String getIdentity() {
        return identityFromId(id);
    }

    public static String identityFromId(Integer id) {
        if (id == null) {
            return null;
        }

        return "product:" + id;
    }
}
