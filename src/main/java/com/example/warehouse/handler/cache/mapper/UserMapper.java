package com.example.warehouse.handler.cache.mapper;

import com.example.warehouse.handler.cache.entity.ImUser;
import com.example.warehouse.http.client.entity.User;

public class UserMapper {

    public static ImUser toImmutable(User user) {
        return new ImUser(user.getUsername(), user.getEmail());
    }

    public static User toMutable(ImUser user) {
        return new User(user.getUsername(), user.getEmail());
    }
}
