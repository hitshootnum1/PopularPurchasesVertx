package com.example.warehouse.http.client.impl.cache;

import com.example.warehouse.handler.cache.CacheManagement;
import com.example.warehouse.handler.cache.entity.ImProduct;
import com.example.warehouse.handler.cache.entity.ImUser;
import com.example.warehouse.handler.cache.mapper.ProductMapper;
import com.example.warehouse.handler.cache.mapper.UserMapper;
import com.example.warehouse.http.client.UserClient;
import com.example.warehouse.http.client.entity.Product;
import com.example.warehouse.http.client.entity.User;
import com.example.warehouse.http.client.impl.UserClientImpl;
import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;

public class UserClientCached implements UserClient {

    private final CacheManagement cache = CacheManagement.getInstance();
    private UserClientImpl client;

    public UserClientCached(Vertx vertx) {
        client = new UserClientImpl(vertx);
    }

    @Override
    public Single<User> userByUsername(String username) {
        ImUser cached = cache.get(User.identityFromId(username), ImUser.class);
        if (cached != null) {
            User user = UserMapper.toMutable(cached);
            return Single.just(user);
        } else {
            return client
                    .userByUsername(username)
                    .doOnSuccess(user -> cache.putIfNotExists(UserMapper.toImmutable(user)));
        }
    }
}
