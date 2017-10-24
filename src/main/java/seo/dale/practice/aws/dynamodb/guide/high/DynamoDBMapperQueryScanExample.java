package seo.dale.practice.aws.dynamodb.guide.high;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBQueryExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.QueryScanExample.html
 */
public class DynamoDBMapperQueryScanExample {
    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();
    static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");

    {
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
    }

    public static void main(String[] args) {
        try {
            DynamoDBMapper mapper = new DynamoDBMapper(client);

            // Get a book - Id=101
            GetBook(mapper, 101);
            // Sample forum and thread to test queries.
            String forumName = "Amazon DynamoDB";
            String threadSubject = "DynamoDB Thread 1";
            // Sample queries.
            FindRepliesInLast15Days(mapper, forumName, threadSubject);
            FindRepliesPostedWithinTimePeriod(mapper, forumName, threadSubject);

            // Scan a table and find book items priced less than specified value.
            FindBooksPricedLessThanSpecifiedValue(mapper, "20");

            // Scan a table with multiple threads and find bicycle items with a specified bicycle type
            int numberOfThreads = 16;
            FindBicyclesOfSpecificTypeWithMultipleThreads(mapper, numberOfThreads, "Road");

            System.out.println("Example complete!");
        }
        catch (Throwable t) {
            System.err.println("Error running the DynamoDBMapperQueryScanExample: " + t);
            t.printStackTrace();
        }
    }

    private static void GetBook(DynamoDBMapper mapper, int i) {
        System.out.println("GetBook: Get book Id='101' ");
        System.out.println("Book table has no sort key. You can do GetItem, but not Query.");
        Book book = mapper.load(Book.class, 101);
        System.out.format("Id = %s Title = %s, ISBN = %s %n", book.getId(), book.getTitle(), book.getISBN());
    }

    public DynamoDBMapperQueryScanExample() {
    }

    private static void FindRepliesInLast15Days(DynamoDBMapper mapper, String forumName, String threadSubject) {
        System.out.println("FindRepliesInLast15Days: Replies within last 15 days.");

        String partitionKey = forumName + "#" + threadSubject;

        long twoWeeksAgoMilli = (new Date()).getTime() - (15L * 24L * 60L * 60L * 1000L);
        Date twoWeeksAgo = new Date();
        twoWeeksAgo.setTime(twoWeeksAgoMilli);

        String twoWeeksAgoStr = dateFormatter.format(twoWeeksAgo);

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(partitionKey));
        eav.put(":val2", new AttributeValue().withS(twoWeeksAgoStr.toString()));

        DynamoDBQueryExpression<Reply> queryExpression = new DynamoDBQueryExpression<Reply>()
                .withKeyConditionExpression("Id = :val1 and ReplyDateTime > :val2")
                .withExpressionAttributeValues(eav);

        List<Reply> latestReplies = mapper.query(Reply.class, queryExpression);
        for (Reply reply : latestReplies) {
            System.out.format("Id=%s, Message=%s, PostedBy=%s %n, ReplyDateTime=%s %n", reply.getId(), reply.getMessage(), reply.getPostedBy(), reply.getReplyDateTime());
        }
    }

    private static void FindRepliesPostedWithinTimePeriod(DynamoDBMapper mapper, String forumName, String threadSubject) {
        System.out.println("FindRepliesPostedWithinTimePeriod: Find replies for thread Message = 'DynamoDB Thread 2' posted within a period.");

        String partitionKey = forumName + "#" + threadSubject;

        long startDateMilli = (new Date()).getTime() - (14L * 24L * 60L * 60L * 1000L); // Two weeks ago
        long endDateMilli = (new Date()).getTime() - (7L * 24L * 60L * 60L * 1000L); // One week ago

        String startDate = dateFormatter.format(startDateMilli);
        String endDate = dateFormatter.format(endDateMilli);

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS(partitionKey));
        eav.put(":val2", new AttributeValue().withS(startDate));
        eav.put(":val3", new AttributeValue().withS(endDate));

        DynamoDBQueryExpression<Reply> queryExpression = new DynamoDBQueryExpression<Reply>()
                .withKeyConditionExpression("Id = :val1 and ReplyDateTime between :val2 and :val3")
                .withExpressionAttributeValues(eav);

        List<Reply> betweenReplies = mapper.query(Reply.class, queryExpression);

        for (Reply reply : betweenReplies) {
            System.out.format("Id=%s, Message=%s, PostedBy=%s %n, PostedDateTime=%s %n", reply.getId(),
                    reply.getMessage(), reply.getPostedBy(), reply.getReplyDateTime());
        }
    }

    private static void FindBooksPricedLessThanSpecifiedValue(DynamoDBMapper mapper, String value) {
        System.out.println("FindBooksPricedLessThanSpecifiedValue: Scan ProductCatalog.");

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withN(value));
        eav.put(":val2", new AttributeValue().withS("Book"));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("Price < :val1 and ProductCategory = :val2")
                .withExpressionAttributeValues(eav);

        List<Book> scanResult = mapper.scan(Book.class, scanExpression);

        for (Book book : scanResult) {
            System.out.println(book);
        }
    }

    private static void FindBicyclesOfSpecificTypeWithMultipleThreads(DynamoDBMapper mapper, int numberOfThreads, String bicycleType) {
        System.out.println("FindBicyclesOfSpecificTypeWithMultipleThreads: Scan ProductCatalog With Multiple Threads.");

        Map<String, AttributeValue> eav = new HashMap<>();
        eav.put(":val1", new AttributeValue().withS("Bicycle"));
        eav.put(":val2", new AttributeValue().withS(bicycleType));

        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withFilterExpression("ProductCategory = :val1 and BicycleType = :val2")
                .withExpressionAttributeValues(eav);

        List<Bicycle> scanResult = mapper.parallelScan(Bicycle.class, scanExpression, numberOfThreads);

        for (Bicycle bicycle : scanResult) {
            System.out.println(bicycle);
        }
    }
}
