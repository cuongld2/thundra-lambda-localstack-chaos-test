package io.thundra.demo.localstack.integration;


import io.thundra.demo.localstack.LocalStackTest;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static io.thundra.demo.localstack.service.Utils.generateShortUuid;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author tolga
 */
public class AppRequestLocalStackTest extends LocalStackTest {

    @Test
    public void testCreateNewRequest() throws IOException {
        // Inject DynamoDB chaos only for "backend_archiveResult" function
//        LambdaServer.registerFunctionEnvironmentInitializer(
//                ChaosInjector.createDynamoDBChaosInjector("http_handleRequest"));


//        String productName = "Test product";
//        Float price = 9.65f;
//        Product productModel = new Product(generateShortUuid(),productName,price);


//        String JSON_STRING = Utilis.parseObjectToJSON(productModel, Product.class);
//        StringEntity requestEntity = new StringEntity(JSON_STRING, ContentType.APPLICATION_JSON);
        CloseableHttpClient httpclient = HttpClients.createDefault();

        HttpPost postMethod = new HttpPost(lambdaUrl);

//        System.out.println(lambdaUrl);
//        System.out.println(requestEntity);
//        postMethod.setEntity(requestEntity);
        System.out.println(postMethod);

        CloseableHttpResponse rawResponse = httpclient.execute(postMethod);

        assertThat(rawResponse.getStatusLine().getStatusCode()).isEqualTo(HttpStatus.SC_OK);
    }
}
