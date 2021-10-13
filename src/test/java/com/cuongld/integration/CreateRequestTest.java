package com.cuongld.integration;


import com.cuongld.ChaosInjector;
import io.thundra.agent.lambda.localstack.LambdaServer;
import com.cuongld.LocalStackTest;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;
import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;


public class CreateRequestTest extends LocalStackTest {

    @Test
    public void testCreateNewRequest() throws IOException {
        LambdaServer.registerFunctionEnvironmentInitializer(
                ChaosInjector.createDynamoDBChaosInjector("createProduct"));

        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost postMethod = new HttpPost(lambdaUrl);

        System.out.println(postMethod);

        CloseableHttpResponse rawResponse = httpclient.execute(postMethod);

        assertThat(rawResponse.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);
    }
}
