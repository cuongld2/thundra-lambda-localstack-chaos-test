package com.cuongld.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sqs.AmazonSQS;
import com.cuongld.model.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.log4j.Logger;
import java.io.IOException;



public class CreateRequestService {

    private Logger logger = Logger.getLogger(this.getClass());
    private static final String PRODUCTS_TABLE_NAME = System.getenv("PRODUCTS_TABLE_NAME");

    private final ObjectMapper mapper = new ObjectMapper();

    private final AmazonSQS sqs;
    private final AmazonDynamoDB dynamoDB;
    private final DynamoDBMapper dynamoDBMapper;
    private final AmazonS3 s3;

    public CreateRequestService() {
        this.sqs = ClientBuilder.buildSQS();
        this.dynamoDB = ClientBuilder.buildDynamoDB();
        this.dynamoDBMapper = buildDynamoDBMapper(dynamoDB);
        this.s3 = ClientBuilder.buildS3();
    }

    private DynamoDBMapper buildDynamoDBMapper(AmazonDynamoDB dynamoDB) {
        return new DynamoDBMapper(dynamoDB, DynamoDBMapperConfig.builder()
                .withTableNameOverride(
                        DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(PRODUCTS_TABLE_NAME))
                .build());
    }


    public void save(Product product) throws IOException {
        logger.info("Products - save(): " + product.toString());
        this.dynamoDBMapper.save(product);
    }

}
