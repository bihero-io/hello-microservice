package io.bihero.hello;

import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;
import io.vertx.ext.web.api.generator.WebApiServiceGen;

@WebApiServiceGen
public interface HelloService {

    static HelloService create(Vertx vertx) {
        return new DefaultHelloService(vertx);
    }

    void getHelloWord(OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

    void getDoc(OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler);

}
