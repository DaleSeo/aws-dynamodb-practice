package seo.dale.practice.aws.dynamodb.guide.high;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

@DynamoDBTable(tableName = "Forum")
public class Forum {
    @DynamoDBHashKey(attributeName = "Name")
    public String name;
    @DynamoDBAttribute(attributeName = "Category")
    public String category;
    @DynamoDBAttribute(attributeName = "Threads")
    public int threads;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getThreads() {
        return threads;
    }

    public void setThreads(int threads) {
        this.threads = threads;
    }
}