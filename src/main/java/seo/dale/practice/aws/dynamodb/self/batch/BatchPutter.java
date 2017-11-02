package seo.dale.practice.aws.dynamodb.self.batch;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.*;

import java.util.*;

public class BatchPutter {
    private static final String TABLE_NAME = "BatchTests";
    private static AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();

    public void batchPut() {
        List<WriteRequest> writeRequests = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, AttributeValue> item = new HashMap<>();
            item.put("id", new AttributeValue().withS(Integer.toString(i)));
            item.put("date", new AttributeValue().withS(new Date().toString()));
            PutRequest putRequest = new PutRequest(item);
            writeRequests.add(new WriteRequest(putRequest));
        }

        Map<String, List<WriteRequest>> requestItems = new HashMap<>();
        requestItems.put(TABLE_NAME, writeRequests);

        BatchWriteItemRequest batchWriteRequest = new BatchWriteItemRequest()
                .withRequestItems(requestItems);

        BatchWriteItemResult result = dynamoDB.batchWriteItem(batchWriteRequest);
        System.out.println("result: " + result);
    }

    public static void main(String[] args) {
        new BatchPutter().batchPut();
    }
}
