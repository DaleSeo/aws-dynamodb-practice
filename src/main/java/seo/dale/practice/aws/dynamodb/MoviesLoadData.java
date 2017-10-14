package seo.dale.practice.aws.dynamodb;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Item;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

/**
 * http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStarted.Java.02.html#GettingStarted.Java.02.02
 */
public class MoviesLoadData {
    public static void main(String[] args) {
        AmazonDynamoDB client = DynamoDbFactory.createClient();
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("Movies");

        try (InputStream inputStream = MoviesLoadData.class.getClassLoader().getResourceAsStream("moviedata.json")) {
            JsonNode rootNode = new ObjectMapper().readTree(inputStream);
            Iterator<JsonNode> iter = rootNode.iterator();

            JsonNode currentNode;
            while (iter.hasNext()) {
                currentNode = iter.next();

                int year = currentNode.path("year").asInt();
                String title = currentNode.path("title").asText();
                String info = currentNode.path("info").toString();

                try {
                    Item item = new Item()
                            .withPrimaryKey("year", year, "title", title)
                            .withJSON("info", info);
                    table.putItem(item);
                    System.out.println("PutItem succeeded: " + year + " " + title);
                } catch (Exception e) {
                    System.err.println("Unable to add movie: " + year + " " + title);
                    System.err.println(e.getMessage());
                    break;
                }
            }
        } catch (IOException e) {
            System.err.println("Failed to open or close the file");
        }
    }
}
