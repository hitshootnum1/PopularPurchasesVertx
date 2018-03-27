package com.example.warehouse.http.client.impl.cache;

import com.example.warehouse.handler.cache.CacheManagement;
import com.example.warehouse.handler.cache.entity.ImProduct;
import com.example.warehouse.http.client.entity.Product;
import com.example.warehouse.http.client.impl.ProductClientImpl;
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
public class ProductClientCachedTest {

    @Mock CacheManagement cache;
    @Mock ProductClientImpl client;
    @Mock Vertx vertx;

    @Test
    public void productInfoHaveNotBeenCached() throws Exception {
        ProductClientCached clientCached = new ProductClientCached(vertx);
        Whitebox.setInternalState(clientCached, "cache", cache);
        Whitebox.setInternalState(clientCached, "client", client);

        Mockito.when(cache.get("product:99999", ImProduct.class))
                .thenReturn(null);

        Product expectedProduct = new Product(99999, "face", 1, 3);
        Mockito.when(client.productInfo(99999)).thenReturn(Single.just(expectedProduct));

        Product product = clientCached.productInfo(99999).blockingGet();
        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo((99999));
        assertThat(product.getFace()).isEqualTo("face");
        assertThat(product.getSize()).isEqualTo(1);
        assertThat(product.getPrice()).isEqualTo(3);
    }

    @Test
    public void productInfoHaveBeenCached() throws Exception {
        ProductClientCached clientCached = new ProductClientCached(vertx);
        Whitebox.setInternalState(clientCached, "cache", cache);

        Mockito.when(cache.get("product:99999", ImProduct.class))
                .thenReturn(new ImProduct(99999, "face", 1, 3));

        Product product = clientCached.productInfo(99999).blockingGet();
        assertThat(product).isNotNull();
        assertThat(product.getId()).isEqualTo(99999);
        assertThat(product.getFace()).isEqualTo("face");
        assertThat(product.getSize()).isEqualTo(1);
        assertThat(product.getPrice()).isEqualTo(3);
    }
}