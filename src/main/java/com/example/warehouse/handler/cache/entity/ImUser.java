package com.example.warehouse.handler.cache.entity;

import com.example.warehouse.handler.IdentifyAble;
import com.example.warehouse.http.client.entity.User;

public class ImUser implements IdentifyAble{
    private String username;
    private String email;

    public ImUser(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String getIdentity() {
        return User.identityFromId(username);
    }
}
