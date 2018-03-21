package com.example.warehouse.http.client.entity;

import com.example.warehouse.handler.IdentifyAble;

import java.util.Date;

public class Purchase implements IdentifyAble{

    private Integer id;
    private Integer productId;
    private String username;
    private Date date;

    public Purchase() {
    }

    public Purchase(Integer id, Integer productId, String username, Date date) {
        this.id = id;
        this.productId = productId;
        this.username = username;
        this.date = date;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public String getIdentity() {
        return identityFromId(id);
    }

    public static String identityFromId(Integer id) {
        if (id == null) {
            return null;
        }

        return "purchase:" + id;
    }
}
