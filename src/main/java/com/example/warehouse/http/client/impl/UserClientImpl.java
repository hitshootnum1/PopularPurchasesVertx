package com.example.warehouse.http.client.impl;

import com.example.warehouse.http.client.UserClient;
import com.example.warehouse.http.client.entity.User;
import com.example.warehouse.http.client.entity.UserInfo;
import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.client.WebClient;

import java.util.concurrent.TimeUnit;

import static com.example.warehouse.MainVerticle.API_HOST;
import static com.example.warehouse.MainVerticle.API_PORT;

public class UserClientImpl implements UserClient {

    private Vertx vertx;
    public UserClientImpl(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public Single<User> userByUsername(String username) {
        return WebClient.create(vertx)
                .get(API_PORT, API_HOST,"/api/users/" + username)
                .rxSend().timeout(2, TimeUnit.SECONDS)
                .map(response -> {
                    UserInfo userInfo = response.bodyAsJsonObject().mapTo(UserInfo.class);
                    User user = userInfo.getUser();
                    if (user == null || user.getUsername() == null) throw new IllegalArgumentException("user not found");
                    return user;
                });
    }

}
