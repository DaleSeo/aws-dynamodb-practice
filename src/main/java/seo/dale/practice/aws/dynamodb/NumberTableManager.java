package seo.dale.practice.aws.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class NumberTableManager {
    private static final Logger log = LoggerFactory.getLogger(NumberTableManager.class);

    private final AmazonDynamoDB dynamoDB;

    public NumberTableManager(AmazonDynamoDB dynamoDB) {
        this.dynamoDB = dynamoDB;
    }

    public void createTable() {
        ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
                .withReadCapacityUnits(5L)
                .withWriteCapacityUnits(5L);

        AttributeDefinition idAttr = new AttributeDefinition()
                .withAttributeName("id")
                .withAttributeType(ScalarAttributeType.S);
        AttributeDefinition dateAttr = new AttributeDefinition()
                .withAttributeName("date")
                .withAttributeType(ScalarAttributeType.N);

        KeySchemaElement hashKey = new KeySchemaElement()
                .withAttributeName("id")
                .withKeyType(KeyType.HASH);
        KeySchemaElement rangeKey = new KeySchemaElement()
                .withAttributeName("date")
                .withKeyType(KeyType.RANGE);

        CreateTableRequest request = new CreateTableRequest()
                .withTableName(Config.TABLE_NAME)
                .withProvisionedThroughput(provisionedThroughput)
                .withAttributeDefinitions(idAttr, dateAttr)
                .withKeySchema(hashKey, rangeKey);

        try {
            dynamoDB.createTable(request);
            waitForTableToBeCreated();
        } catch (ResourceInUseException e) {
            log.info("table already exists");
        }
    }

    private void waitForTableToBeCreated() {
        long endTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5);
        while (System.currentTimeMillis() < endTime) {
            sleepInSeconds(5);
            DescribeTableResult result = dynamoDB.describeTable(Config.TABLE_NAME);
            if ("ACTIVE".equals(result.getTable().getTableStatus())) {
                log.info("successfully created\n{}", result);
                return;
            }
            log.debug("still creating table...\n{}", result);
        }

        throw new RuntimeException("Failed to create table");
    }

    public void deleteTable() {
        try {
            dynamoDB.deleteTable(Config.TABLE_NAME);
            waitForTableToBeDeleted();
        } catch (ResourceNotFoundException e) {
            log.info("table doesn't exists");
        }
    }

    private void waitForTableToBeDeleted() {
        long endTime = System.currentTimeMillis() + TimeUnit.MINUTES.toMillis(5);
        while (System.currentTimeMillis() < endTime) {
            sleepInSeconds(5);
            try {
                DescribeTableResult result = dynamoDB.describeTable(Config.TABLE_NAME);
                log.debug("still deleting table\n{}", result);
            } catch (ResourceNotFoundException e) {
                log.info("successfully deleted");
                return;
            }
        }

        throw new RuntimeException("Failed to delete table");
    }

    public static void sleepInSeconds(long seconds) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
