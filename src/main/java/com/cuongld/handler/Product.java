package com.cuongld.handler;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cuongld.service.CreateRequestService;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static com.cuongld.service.Utils.generateShortUuid;


public class Product implements RequestHandler<APIGatewayProxyRequestEvent, APIGatewayProxyResponseEvent> {

    private static final Logger logger = LogManager.getLogger(Product.class);
    private static final Map<String, String> headers = new HashMap<String, String>() {{
        put("content-type", "application/json");
    }};

    private final ObjectMapper mapper = new ObjectMapper();
    private CreateRequestService createRequestService = new CreateRequestService();

    @Override
    public APIGatewayProxyResponseEvent handleRequest(APIGatewayProxyRequestEvent request, Context context) {
        try {
            logger.info("Request --> " + request);
            if ("/products".equals(request.getPath()) && "POST".equals(request.getHttpMethod())) {
                return startNewRequest();
            } else {
                return new APIGatewayProxyResponseEvent().
                        withStatusCode(404);
            }
        } catch (Exception e) {
            logger.error("Error occurred handling message. Exception is ", e);
            return new APIGatewayProxyResponseEvent().
                    withStatusCode(500).
                    withBody(e.getMessage());
        }
    }

    private APIGatewayProxyResponseEvent startNewRequest() throws IOException {
        // put message onto SQS queue

        // create the Product object for post
        String id = generateShortUuid();
        com.cuongld.model.Product product = new com.cuongld.model.Product(id,"test",4.5f);
        // product.setId(body.get("id").asText());
        createRequestService.save(product);
        return new APIGatewayProxyResponseEvent().
                withStatusCode(200).
                withHeaders(headers).
                withBody(mapper.writeValueAsString(new com.cuongld.model.Product(id,"test",4.5f)));
    }

}
