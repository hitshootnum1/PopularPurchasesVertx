package com.example.warehouse.http.client;

import com.example.warehouse.http.client.entity.User;
import io.reactivex.Single;

public interface UserClient {
    Single<User> userByUsername(String username);
}
