package seo.dale.practice.aws.dynamodb.self.batch;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.*;

public class BatchDeleter {
    private static final String TABLE_NAME = "BatchTests";
    private static AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();

    public void batchDelete() {
        List<WriteRequest> writeRequests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, AttributeValue> key = new HashMap<>();
            key.put("id", new AttributeValue().withS(Integer.toString(i)));
            DeleteRequest deleteRequest = new DeleteRequest(key);
            writeRequests.add(new WriteRequest(deleteRequest));
        }

        Map<String, List<WriteRequest>> requestItems = new HashMap<>();
        requestItems.put(TABLE_NAME, writeRequests);

        BatchWriteItemRequest batchWriteRequest = new BatchWriteItemRequest()
                .withRequestItems(requestItems);

        BatchWriteItemResult result = dynamoDB.batchWriteItem(batchWriteRequest);
        System.out.println("result: " + result);
    }

    public static void main(String[] args) {
        new BatchDeleter().batchDelete();
    }
}
