package io.bihero.hello;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.api.OperationRequest;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(VertxExtension.class)
public class HelloServiceTest {

    private HelloService helloService = HelloService.create(Vertx.vertx());

    @Test
    @DisplayName("Test 'getHelloWord' method returns 'Hello' word")
    public void testHelloMethod(VertxTestContext testContext) {
        helloService.getHelloWord(new OperationRequest(new JsonObject()), testContext.succeeding(it -> {
            assertThat(it.getStatusCode()).isEqualTo(200);
            assertThat(it.getPayload().toString()).isEqualTo("Hello");
            testContext.completeNow();
        }));
    }

    @Test
    @DisplayName("Test 'getDoc' method returns service documentation in OpenAPI format")
    public void testDocMethod(VertxTestContext testContext) {
        helloService.getDoc(new OperationRequest(new JsonObject()), testContext.succeeding(it -> {
            try {
                assertThat(it.getStatusCode()).isEqualTo(200);
                assertThat(it.getPayload().toString()).isEqualTo(IOUtils.toString(this.getClass()
                        .getResourceAsStream("../../../doc.yaml"), "UTF-8"));
                testContext.completeNow();
            } catch (IOException e) {
                testContext.failNow(e);
            }
        }));
    }

}
