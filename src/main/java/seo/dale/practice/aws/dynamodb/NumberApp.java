package seo.dale.practice.aws.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

public class NumberApp {
    public static void main(String[] args) {
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
        NumberTableManager tableManager = new NumberTableManager(dynamoDB);

        // recreate table
        tableManager.deleteTable();
        tableManager.createTable();
    }
}
