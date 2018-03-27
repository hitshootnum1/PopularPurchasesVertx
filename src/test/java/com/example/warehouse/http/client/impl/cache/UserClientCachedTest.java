package com.example.warehouse.http.client.impl.cache;

import com.example.warehouse.handler.cache.CacheManagement;
import com.example.warehouse.handler.cache.entity.ImUser;
import com.example.warehouse.http.client.entity.User;
import com.example.warehouse.http.client.impl.UserClientImpl;
import io.reactivex.Single;
import io.vertx.reactivex.core.Vertx;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.powermock.reflect.Whitebox;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(MockitoJUnitRunner.class)
public class UserClientCachedTest {

    @Mock CacheManagement cache;
    @Mock UserClientImpl client;
    @Mock Vertx vertx;

    @Test
    public void userByUsernameHaveNotBeenCached() throws Exception {
        UserClientCached clientCached = new UserClientCached(vertx);
        Whitebox.setInternalState(clientCached, "cache", cache);
        Whitebox.setInternalState(clientCached, "client", client);

        Mockito.when(cache.get("user:user1", ImUser.class))
                .thenReturn(null);

        User expectedUser = new User("user1", "user1@gmail.com");
        Mockito.when(client.userByUsername("user1")).thenReturn(Single.just(expectedUser));

        User user = clientCached.userByUsername("user1").blockingGet();
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("user1");
        assertThat(user.getEmail()).isEqualTo("user1@gmail.com");
    }

    @Test
    public void userByUsernameHaveBeenCached() throws Exception {
        UserClientCached clientCached = new UserClientCached(vertx);
        Whitebox.setInternalState(clientCached, "cache", cache);
        Whitebox.setInternalState(clientCached, "client", client);

        Mockito.when(cache.get("user:user1", ImUser.class))
                .thenReturn(new ImUser("user1", "user1@gmail.com"));

        User user = clientCached.userByUsername("user1").blockingGet();
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("user1");
        assertThat(user.getEmail()).isEqualTo("user1@gmail.com");

    }
}