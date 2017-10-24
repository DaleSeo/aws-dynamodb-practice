package seo.dale.practice.aws.dynamodb.guide.document;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.Arrays;

/**
 * http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStarted.Java.01.html
 */
public class MoviesCreateTable {
    public static void main(String[] args) {
        AmazonDynamoDB client = DynamoDbFactory.createClient();
        DynamoDB dynamoDB = new DynamoDB(client);

        String tableName = "Movies";

        try {
            System.out.println("Attempting to create table; please wait...");
            Table table = dynamoDB.createTable(
                    tableName,
                    Arrays.asList(
                            new KeySchemaElement("year", KeyType.HASH),
                            new KeySchemaElement("title", KeyType.RANGE)
                    ),
                    Arrays.asList(
                            new AttributeDefinition("year", ScalarAttributeType.N),
                            new AttributeDefinition("title", ScalarAttributeType.S)
                    ),
                    new ProvisionedThroughput(1L, 1L)
            );
            table.waitForActive();
            System.out.println("Success. Table status: " + table.getDescription().getTableStatus());
        } catch (Exception e) {
            System.err.println("Unable to create table: ");
            System.err.println(e.getMessage());
        }
    }
}