package com.example.warehouse.http.client.entity;

import com.example.warehouse.handler.IdentifyAble;

public class User implements IdentifyAble {

    private String username;
    private String email;

    public User() {
    }

    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String getIdentity() {
        return identityFromId(username);
    }

    public static String identityFromId(String username) {
        if (username == null) {
            return  null;
        }

        return "user:" + username;
    }
}
