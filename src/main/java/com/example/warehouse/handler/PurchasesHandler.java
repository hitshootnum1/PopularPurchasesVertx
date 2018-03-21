package com.example.warehouse.handler;

import com.example.warehouse.dto.PopularPurchaseResponse;
import com.example.warehouse.http.client.ProductClient;
import com.example.warehouse.http.client.PurchaseClient;
import com.example.warehouse.http.client.UserClient;
import com.example.warehouse.http.client.entity.Purchase;
import com.example.warehouse.http.client.entity.RecentPurchaseByUser;
import com.example.warehouse.http.client.impl.cache.ProductClientCached;
import com.example.warehouse.http.client.impl.cache.PurchaseClientCached;
import com.example.warehouse.http.client.impl.cache.UserClientCached;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.reactivex.Single;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.ext.web.RoutingContext;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

public class PurchasesHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // External API client interfaces
    private PurchaseClient purchaseClient;
    private ProductClient productClient;
    private UserClient userClient;

    /**
     * For cached purpose, a decorator is created for each external API client, after retrieving data from APIs
     * Information is putted in to cache
     * @param vertx
     */
    public PurchasesHandler(Vertx vertx) {
        purchaseClient = new PurchaseClientCached(vertx);
        productClient = new ProductClientCached(vertx);
        userClient = new UserClientCached(vertx);
    }

    /**
     * A controller receives requests and produces json result
     * @param routingContext
     */
    public void popularPurchases(RoutingContext routingContext) {
        // getting username param
        String username = routingContext.request().getParam("username");
        userClient
                .userByUsername(username) // checking user exists
                .flatMap(user -> purchaseClient.recentPurchasesByUser(username)) // getting purchases by username
                .subscribe(
                        recentPurchaseByUser -> okHandler(routingContext, username, recentPurchaseByUser), // getting result without errors
                        throwable -> errorHandler(routingContext, username, throwable)); // exceptions happen
    }

    private void okHandler(RoutingContext routingContext, String username, RecentPurchaseByUser recentPurchaseByUser) {
        List<Purchase> purchases = recentPurchaseByUser.getPurchases();
        if (purchases == null || purchases.isEmpty()) {
            logger.warn("Username '" + username + "' doesn't have any purchase");
            routingContext.response()
                    .setStatusCode(200)
                    .putHeader("Content-Type", "application/json")
                    .end("[]");
        } else {
            List<Single<PopularPurchaseResponse>> singles = purchases.stream()
                    .map(purchase -> getPurchaseByProduct(purchase.getProductId()))
                    .collect(toList());

            Single.zip(singles, this::sortedPurchases)
                    .subscribe(response -> {
                        routingContext.response()
                                .setStatusCode(200)
                                .putHeader("Content-Type", "application/json")
                                .end(response);
                    });
        }
    }

    private void errorHandler(RoutingContext routingContext, String username, Throwable throwable) {
        if (throwable instanceof IllegalArgumentException) {
            routingContext.response()
                    .setStatusCode(404)
                    .putHeader("Content-Type", "application/json")
                    .end("{\"error\" : \"User with username of '" + username + "' was not found\"}");
        } else {
            routingContext.response()
                    .setStatusCode(500)
                    .putHeader("Content-Type", "application/json")
                    .end("{\"error\" : \"" + throwable.getMessage() + "\"}");
        }
    }

    /**
     *
     * - second and third step "get list of all people of purchase for the product & product info"
     * - fourth step "finally, put all of the data together and sort it so that the product with the highest
     * number of recent purchases is first."
     * @param productId
     * @return
     */
    private Single<PopularPurchaseResponse> getPurchaseByProduct(Integer productId) {
        return Single.zip(
                purchaseClient.purchasesByProduct(productId), // second step
                productClient.productInfo(productId), // third step
                (recentPurchaseByProduct, product) -> {
                    PopularPurchaseResponse popularPurchase = new PopularPurchaseResponse();

                    // fourth step sorting the most recent
                    List<String> usernameList = recentPurchaseByProduct.getPurchases()
                            .stream()
                            .map(Purchase::getUsername)
                            .distinct()
                            .collect(toList());
                    popularPurchase.setRecent(usernameList);

                    popularPurchase.setId(product.getId());
                    popularPurchase.setFace(product.getFace());
                    popularPurchase.setSize(product.getSize());

                    return popularPurchase;
                });
    }

    private String sortedPurchases(Object[] responses) throws JsonProcessingException {
        List<PopularPurchaseResponse> sorted = Stream.of(responses)
                .map(response -> (PopularPurchaseResponse) response)
                .sorted(Comparator.comparing(purchase -> purchase.getRecent().size(), Comparator.reverseOrder()))
                .collect(toList());

        return Json.mapper.writeValueAsString(sorted);
    }
}
