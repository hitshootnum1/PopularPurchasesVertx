package com.example.warehouse;


import com.example.warehouse.handler.PurchasesHandler;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.reactivex.core.AbstractVerticle;
import io.vertx.reactivex.core.Vertx;
import io.vertx.reactivex.core.http.HttpServer;
import io.vertx.reactivex.ext.web.Router;
import org.apache.commons.cli.*;

/**
 * Bootstrap class, A verticle in Vert.x represent for a executor of everything (calculating, IO, ...)
 */
public class MainVerticle extends AbstractVerticle {

    private static String VERTICLE_ID;
    private static Vertx VERTX;

    public static String API_HOST = "localhost";
    public static int API_PORT = 8000;
    public static int SERVER_PORT = 8080;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    public static void deploy(String[] args) {
        main(args);
    }

    public static void unDeploy() {
        VERTX.rxUndeploy(VERTICLE_ID)
                .subscribe(() -> {
                    System.out.println("server is un deployed");
                });
    }

    /**
     * Creating a vert.x instance and deploy a simple verticle, with reactive Java (rxJava)
     * @param args
     */
    public static void main(String[] args) {
        initGlobalVariables(args);

        VERTX = Vertx.vertx();

        VERTX.rxDeployVerticle(MainVerticle.class.getName())
                .subscribe(id -> {
                    VERTICLE_ID = id;
                    System.out.println("component id: " + id);
                });
    }

    @Override
    public void start() throws Exception {

        Router router = Router.router(vertx);
        // Handler handle requests
        PurchasesHandler handler = new PurchasesHandler(vertx);

        // route matched request to proper handler
        router.get("/api/recent_purchases/:username")
                .handler(handler::popularPurchases);

        HttpServer server = vertx.createHttpServer();
        server.requestStream()
                .toFlowable()
                .subscribe(router::accept);

        // http server listen port
        server.listen(SERVER_PORT);

        logger.info("Http server deployed on port " + SERVER_PORT + "!");
    }

    public static void initGlobalVariables(String args []) {
        Options options = new Options();

        Option apiHost = new Option("ah", "apiHost", true, "external api host");
        apiHost.setRequired(false);
        options.addOption(apiHost);

        Option apiPort = new Option("ap", "apiPort", true, "external api port");
        apiPort.setRequired(false);
        options.addOption(apiPort);

        Option serverPort = new Option("p", "serverPort", true, "server port");
        serverPort.setRequired(false);
        options.addOption(serverPort);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
            return;
        }

        if (cmd.getOptionValue("ah") != null) API_HOST = cmd.getOptionValue("ah");
        if (cmd.getOptionValue("ap") != null) API_PORT = Integer.parseInt(cmd.getOptionValue("ap"));
        if (cmd.getOptionValue("p") != null) SERVER_PORT = Integer.parseInt(cmd.getOptionValue("p"));
    }
}
