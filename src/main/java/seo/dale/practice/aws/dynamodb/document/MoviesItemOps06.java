package seo.dale.practice.aws.dynamodb.document;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.document.DynamoDB;
import com.amazonaws.services.dynamodbv2.document.Table;
import com.amazonaws.services.dynamodbv2.document.spec.DeleteItemSpec;

/**
 * http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/GettingStarted.Java.03.html#GettingStarted.Java.03.06
 */
public class MoviesItemOps06 {
    public static void main(String[] args) {
        AmazonDynamoDB client = DynamoDbFactory.createClient();
        DynamoDB dynamoDB = new DynamoDB(client);

        Table table = dynamoDB.getTable("Movies");

        int year = 2015;
        String title = "The Big New Movies";

        DeleteItemSpec spec = new DeleteItemSpec()
                .withPrimaryKey("year", year, "title", title);
//                .withConditionExpression("info.rating <= :val")
//                .withValueMap(new ValueMap().withNumber(":val", 5));

        try {
            System.out.println("Attempting a conditional delete...");
            table.deleteItem(spec);
            System.out.println("DeleteItem succeeded");
        } catch (Exception e) {
            System.err.println("Unable to delete item: " + year + " " + title);
            System.err.println(e.getMessage());
        }
    }
}
