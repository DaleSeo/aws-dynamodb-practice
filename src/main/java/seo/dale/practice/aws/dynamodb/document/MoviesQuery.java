package seo.dale.practice.aws.dynamodb.document;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.*;
import com.amazonaws.services.dynamodbv2.document.spec.QuerySpec;

import java.util.HashMap;
import java.util.Map;

/**
 * http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStarted.Java.04.html#GettingStarted.Java.04.Query
 */
public class MoviesQuery {
    public static void main(String[] args) {
        AmazonDynamoDB client = DynamoDbFactory.createClient();
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("Movies");

        Map<String, String> nameMap = new HashMap<>();
        nameMap.put("#yr", "year");

        Map<String, Object> valueMap = new HashMap<>();
        valueMap.put(":yyyy", 1985);

        QuerySpec querySpec = new QuerySpec()
                .withKeyConditionExpression("#yr = :yyyy")
                .withNameMap(nameMap)
                .withValueMap(valueMap);

        try {
            System.out.println("Movies from 1985");
            ItemCollection<QueryOutcome> items = table.query(querySpec);
            for (Item item : items) {
                System.out.println(item.getNumber("year") + ": " + item.getString("title"));
            }
        } catch (Exception e) {
            System.err.println("Unable to query movies from 1985");
            System.err.println(e.getMessage());
        }

        valueMap.put(":yyyy", 1992);
        valueMap.put(":letter1", "A");
        valueMap.put(":letter2", "L");

        querySpec.withProjectionExpression("#yr, title, info.genres, info.actors[0]")
                .withKeyConditionExpression("#yr = :yyyy and title between :letter1 and :letter2").withNameMap(nameMap)
                .withValueMap(valueMap);

        try {
            System.out.println("Movies from 1992 - titles A-L, with genres and lead actor");
            ItemCollection<QueryOutcome> items = table.query(querySpec);

            for (Item item : items) {
                System.out.println(item.getNumber("year") + ": " + item.getString("title") + item.getMap("info"));
            }
        }
        catch (Exception e) {
            System.err.println("Unable to query movies from 1992:");
            System.err.println(e.getMessage());
        }
    }
}
