package io.bihero.hello;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.eventbus.MessageConsumer;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.api.contract.openapi3.OpenAPI3RouterFactory;
import io.vertx.serviceproxy.ServiceBinder;

public class HelloVerticle extends AbstractVerticle {

    private HttpServer server;
    private MessageConsumer<JsonObject> consumer;

    @Override
    public void start(Promise<Void> promise) {
        startHelloService();
        startHttpServer().future().setHandler(promise);
    }

    /**
     * This method closes the http server and unregister all services loaded to Event Bus
     */
    @Override
    public void stop(){
        this.server.close();
        consumer.unregister();
    }

    private void startHelloService() {
        consumer = new ServiceBinder(vertx).setAddress("service.hello")
                .register(HelloService.class, HelloService.create(getVertx()));
    }

    /**
     * This method constructs the router factory, mounts services and handlers and starts the http server
     * with built router
     * @return
     */
    private Promise<Void> startHttpServer() {
        Promise<Void> promise = Promise.promise();
        OpenAPI3RouterFactory.create(this.vertx, "/doc.yaml", openAPI3RouterFactoryAsyncResult -> {
            if (openAPI3RouterFactoryAsyncResult.succeeded()) {
                OpenAPI3RouterFactory routerFactory = openAPI3RouterFactoryAsyncResult.result();

                // Mount services on event bus based on extensions
                routerFactory.mountServicesFromExtensions();

                // Generate the router
                Router router = routerFactory.getRouter();

                int port = config().getInteger("serverPort", 8080);
                String host = config().getString("serverHost", "localhost");

                server = vertx.createHttpServer(new HttpServerOptions().setPort(port).setHost(host));
                server.requestHandler(router).listen(ar -> {
                    // Error starting the HttpServer
                    if (ar.succeeded()) promise.complete();
                    else promise.fail(ar.cause());
                });
            } else {
                // Something went wrong during router factory initialization
                promise.fail(openAPI3RouterFactoryAsyncResult.cause());
            }
        });
        return promise;
    }

}
