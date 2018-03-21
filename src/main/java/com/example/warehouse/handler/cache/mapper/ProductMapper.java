package com.example.warehouse.handler.cache.mapper;

import com.example.warehouse.handler.cache.entity.ImProduct;
import com.example.warehouse.http.client.entity.Product;

public class ProductMapper {

    public static ImProduct toImmutable(Product product) {
        return new ImProduct(product.getId(), product.getFace(), product.getSize(), product.getPrice());
    }

    public static Product toMutable(ImProduct product) {
        return new Product(product.getId(), product.getFace(), product.getSize(), product.getPrice());
    }
}
