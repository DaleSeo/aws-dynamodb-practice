package seo.dale.practice.aws.dynamodb.streams;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StreamsAdapterDemoHelper {

    public static String createTable(AmazonDynamoDB client, String tableName) {
        List<AttributeDefinition> attributeDefinitions = new ArrayList<>();
        attributeDefinitions.add(new AttributeDefinition()
                .withAttributeName("Id")
                .withAttributeType("N"));

        List<KeySchemaElement> keySchema = new ArrayList<>();
        keySchema.add(new KeySchemaElement()
                .withAttributeName("Id")
                .withKeyType(KeyType.HASH));

        ProvisionedThroughput provisionedThroughput = new ProvisionedThroughput()
                .withReadCapacityUnits(2L)
                .withWriteCapacityUnits(2L);

        StreamSpecification streamSpecification = new StreamSpecification();
        streamSpecification.setStreamEnabled(true);
        streamSpecification.setStreamViewType(StreamViewType.NEW_IMAGE);

        CreateTableRequest createTableRequest = new CreateTableRequest()
                .withTableName(tableName)
                .withAttributeDefinitions(attributeDefinitions)
                .withKeySchema(keySchema)
                .withProvisionedThroughput(provisionedThroughput)
                .withStreamSpecification(streamSpecification);

        try {
            System.out.println("Creating table " + tableName);
            CreateTableResult result = client.createTable(createTableRequest);
            return result.getTableDescription().getLatestStreamArn();
        }
        catch (ResourceInUseException e) {
            System.out.println("Table already exists.");
            return describeTable(client, tableName).getTable().getLatestStreamArn();
        }
    }

    public static DescribeTableResult describeTable(AmazonDynamoDB client, String tableName) {
        return client.describeTable(new DescribeTableRequest().withTableName(tableName));
    }

    public static ScanResult scanTable(AmazonDynamoDB client, String tableName) {
        return client.scan(new ScanRequest().withTableName(tableName));
    }

    public static void putItem(AmazonDynamoDB client, String tableName, String id, String val) {
        Map<String, AttributeValue> item = new HashMap<>();
        item.put("Id", new AttributeValue().withN(id));
        item.put("attribute-1", new AttributeValue().withS(val));

        PutItemRequest putItemRequest = new PutItemRequest()
                .withTableName(tableName)
                .withItem(item);
        client.putItem(putItemRequest);
    }

    public static void putItem(AmazonDynamoDB client, String tableName, Map<String, AttributeValue> item) {
        PutItemRequest putItemRequest = new PutItemRequest()
                .withTableName(tableName)
                .withItem(item);
        client.putItem(putItemRequest);
    }

    public static void updateItem(AmazonDynamoDB client, String tableName, String id, String val) {
        Map<String, AttributeValue> key = new HashMap<>();
        key.put("Id", new AttributeValue().withN(id));

        Map<String, AttributeValueUpdate> attributeUpdates = new HashMap<>();
        AttributeValueUpdate update = new AttributeValueUpdate()
                .withAction(AttributeAction.PUT)
                .withValue(new AttributeValue().withS(val));
        attributeUpdates.put("attribute-2", update);

        UpdateItemRequest updateItemRequest = new UpdateItemRequest()
                .withTableName(tableName)
                .withKey(key)
                .withAttributeUpdates(attributeUpdates);

        client.updateItem(updateItemRequest);
    }

    public static void deleteItem(AmazonDynamoDB client, String tableName, String id) {
        java.util.Map<String, AttributeValue> key = new HashMap<>();
        key.put("Id", new AttributeValue().withN(id));

        DeleteItemRequest deleteItemRequest = new DeleteItemRequest()
                .withTableName(tableName)
                .withKey(key);
        client.deleteItem(deleteItemRequest);
    }
}
