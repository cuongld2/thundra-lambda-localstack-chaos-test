package io.thundra.demo.localstack.service;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.sqs.AmazonSQS;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.thundra.demo.localstack.model.Product;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import static io.thundra.demo.localstack.service.ClientBuilder.*;

/**
 * @author tolga
 */
public class AppRequestService {

//    public static final String REQUEST_QUEUE_URL =
//            ClientBuilder.normalizeUrl(System.getenv("REQUEST_QUEUE_URL"));
//    public static final String APP_REQUESTS_TABLE_NAME = System.getenv("APP_REQUESTS_TABLE_NAME");
    private Logger logger = Logger.getLogger(this.getClass());
    private static final String PRODUCTS_TABLE_NAME = System.getenv("PRODUCTS_TABLE_NAME");

    private final ObjectMapper mapper = new ObjectMapper();

    private final AmazonSQS sqs;
    private final AmazonDynamoDB dynamoDB;
    private final DynamoDBMapper dynamoDBMapper;
    private final AmazonS3 s3;

    public AppRequestService() {
        this.sqs = buildSQS();
        this.dynamoDB = buildDynamoDB();
        this.dynamoDBMapper = buildDynamoDBMapper(dynamoDB);
        this.s3 = buildS3();
    }

    private DynamoDBMapper buildDynamoDBMapper(AmazonDynamoDB dynamoDB) {
        return new DynamoDBMapper(dynamoDB, DynamoDBMapperConfig.builder()
                .withTableNameOverride(
                        DynamoDBMapperConfig.TableNameOverride.withTableNameReplacement(PRODUCTS_TABLE_NAME))
                .build());
    }

    public Boolean ifTableExists() {
        return this.dynamoDB.describeTable(PRODUCTS_TABLE_NAME).getTable().getTableStatus().equals("ACTIVE");
    }

    public List<Product> list() throws IOException {
        DynamoDBScanExpression scanExp = new DynamoDBScanExpression();
        List<Product> results = this.dynamoDBMapper.scan(Product.class, scanExp);
        for (Product p : results) {
            logger.info("Products - list(): " + p.toString());
        }
        return results;
    }

    public Product get(String id) throws IOException {
        Product product = null;

        HashMap<String, AttributeValue> av = new HashMap<String, AttributeValue>();
        av.put(":v1", new AttributeValue().withS(id));

        DynamoDBQueryExpression<Product> queryExp = new DynamoDBQueryExpression<Product>()
                .withKeyConditionExpression("id = :v1")
                .withExpressionAttributeValues(av);

        PaginatedQueryList<Product> result = this.dynamoDBMapper.query(Product.class, queryExp);
        if (result.size() > 0) {
            product = result.get(0);
            logger.info("Products - get(): product - " + product.toString());
        } else {
            logger.info("Products - get(): product - Not Found.");
        }
        return product;
    }

    public void save(Product product) throws IOException {
        logger.info("Products - save(): " + product.toString());
        this.dynamoDBMapper.save(product);
    }

    public Boolean delete(String id) throws IOException {
        Product product = null;

        // get product if exists
        product = get(id);
        if (product != null) {
            logger.info("Products - delete(): " + product.toString());
            this.dynamoDBMapper.delete(product);
        } else {
            logger.info("Products - delete(): product - does not exist.");
            return false;
        }
        return true;
    }


}
