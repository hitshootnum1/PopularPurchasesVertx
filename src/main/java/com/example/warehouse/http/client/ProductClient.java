package com.example.warehouse.http.client;

import com.example.warehouse.http.client.entity.Product;
import io.reactivex.Single;

public interface ProductClient {
    Single<Product> productInfo(Integer productId);
}
