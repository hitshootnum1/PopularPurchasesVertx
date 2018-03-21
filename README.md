A "Popular purchases" API implemented by 
<a href="http://vertx.io/">Vert.x</a> 
and <a href="https://github.com/ReactiveX/RxJava">reactive Java</a>

<b>To start server:</b>
1. Install Java JDK 8 + <a href="https://maven.apache.org/">Maven</a>
2. Unzip project
3. `mvn clean install`
4. To start API server: `java -cp target/PopularPurchasesVerxt-1.0-SNAPSHOT.jar com.example.warehouse.MainVerticle -p 8080 -ah localhost -ap 8000`
+   `p`: api server port
+   `ah`: external API host
+   `ap`: external API port

<h4>Note</h4>
1. To archive a better response time and throughput. I decided to use a non blocking IO library (Vertx) 
which supports completely non blocking processing model included non blocking HTTP Server and HTTP client.
2. The implementation of external API(not README.md) only provides readonly APIs. 
So external API stores same data every time since it starts. That makes possible when caching all of three apis
<br>
`GET /api/purchases/by_user/:username?limit=5`<br>
`GET /api/purchases/by_product/:product_id`<br>
`GET /api/products/:product_id`<br>

3. Instead of caching only one front-end api `/api/recent_purchases/:username​` which could leads to duplicate/redundant 
data. A caching of 3 back-end apis deals with that problem but taking a little bit acceptable overhead.
4. Decorator pattern is applied for caching implementation of each external API client.
5. A concurrent `Map<String, Object>` includes object's ids as keys represent for the memory cache and secured as a `CacheManagement` singleton instance.
Data from three apis is stored in only a singleton instance help centralizing caching management.
6. Values from `Map<String, Object>` are all immutable object since external API only provides readonly APIs   
7. Using rxJava help asynchronous implementation looks easier to read and avoid callback hell.

