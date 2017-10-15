package seo.dale.practice.aws.dynamodb.document;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

/**
 * http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStarted.Java.Summary.html
 */
public class DynamoDbFactory {
    public static AmazonDynamoDB createClient() {
        return AmazonDynamoDBClientBuilder
                .standard()
                .withRegion(Regions.US_WEST_2)
                .build();
    }
}
