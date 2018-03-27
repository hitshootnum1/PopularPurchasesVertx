package com.example.warehouse.dto;

import java.util.List;

public class PopularPurchaseResponse {

    // product properties
    private Integer id;
    private String face;
    private Integer size;

    // recent usernames
    private List<String> recent;

    public PopularPurchaseResponse(Integer id, String face, Integer size, List<String> recent) {
        this.id = id;
        this.face = face;
        this.size = size;
        this.recent = recent;
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

    public List<String> getRecent() {
        return recent;
    }
}
