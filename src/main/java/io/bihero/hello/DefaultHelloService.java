package io.bihero.hello;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.ext.web.api.OperationResponse;

public class DefaultHelloService implements HelloService {

    private final Vertx vertx;

    public DefaultHelloService(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    public void getHelloWord(OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        resultHandler.handle(Future.succeededFuture(OperationResponse.completedWithPlainText(Buffer.buffer("Hello"))));
    }

    @Override
    public void getDoc(OperationRequest context, Handler<AsyncResult<OperationResponse>> resultHandler) {
        vertx.fileSystem().readFile("doc.yaml", buffResult ->
                resultHandler.handle(Future.succeededFuture(
                        OperationResponse.completedWithPlainText(buffResult.result()))
                ));
    }

}