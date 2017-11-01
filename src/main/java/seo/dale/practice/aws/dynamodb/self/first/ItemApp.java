package seo.dale.practice.aws.dynamodb.self.first;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

public class ItemApp {
    public static void main(String[] args) {
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper dbMapper = new DynamoDBMapper(dynamoDB);

        TableManager tableManager = new TableManager(dynamoDB);

        // recreate table
        tableManager.deleteTable();
        tableManager.createTable();

        // put records
        new Thread(() -> {
            new ItemPutter(dbMapper).put();
        }).start();
    }
}
