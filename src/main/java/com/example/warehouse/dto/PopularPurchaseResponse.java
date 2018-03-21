package com.example.warehouse.dto;

import java.util.List;

public class PopularPurchaseResponse {

    // product properties
    private Integer id;
    private String face;
    private Integer size;

    // recent usernames
    private List<String> recent;

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

    public List<String> getRecent() {
        return recent;
    }

    public void setRecent(List<String> recent) {
        this.recent = recent;
    }
}
