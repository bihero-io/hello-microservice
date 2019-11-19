package io.bihero.hello;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import io.vertx.junit5.Checkpoint;
import io.vertx.junit5.VertxExtension;
import io.vertx.junit5.VertxTestContext;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(VertxExtension.class)
public class HelloVerticleTest {

    @Test
    @DisplayName("Test that verticle is up and respond me by 'Hello' word and doc in OpenAPI format")
    public void testHelloVerticle(Vertx vertx, VertxTestContext testContext) {
        WebClient webClient = WebClient.create(vertx);

        Checkpoint deploymentCheckpoint = testContext.checkpoint();
        Checkpoint requestCheckpoint = testContext.checkpoint(2);

        HelloVerticle verticle = spy(new HelloVerticle());
        JsonObject config = new JsonObject().put("serverPort", 8081).put("serverHost", "0.0.0.0");
        doReturn(config).when(verticle).config();
        vertx.deployVerticle(verticle, testContext.succeeding(id -> {
            deploymentCheckpoint.flag();
            // test GET /
            webClient.get(8081, "localhost", "/")
                    .as(BodyCodec.string())
                    .send(testContext.succeeding(resp -> {
                        assertThat(resp.body()).isEqualTo("Hello");
                        assertThat(resp.statusCode()).isEqualTo(200);
                        requestCheckpoint.flag();
                    }));
            // test GET /doc
            webClient.get(8081, "localhost", "/doc")
                    .as(BodyCodec.string())
                    .send(testContext.succeeding(resp -> {
                        try {
                            assertThat(resp.body()).isEqualTo(IOUtils.toString(this.getClass()
                                    .getResourceAsStream("../../../doc.yaml"), "UTF-8"));
                            assertThat(resp.statusCode()).isEqualTo(200);
                            requestCheckpoint.flag();
                        } catch (Exception e) {
                            requestCheckpoint.flag();
                            testContext.failNow(e);
                        }
                    }));
        }));
    }

}
