package seo.dale.practice.aws.dynamodb.streams;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorFactory;

public class StreamsRecordProcessorFactory implements IRecordProcessorFactory {
    private final String tableName;

    public StreamsRecordProcessorFactory(String tableName) {
        this.tableName = tableName;
    }

    @Override
    public IRecordProcessor createProcessor() {
        AmazonDynamoDB dynamoDBClient = AmazonDynamoDBClientBuilder.standard().build();
        return new StreamsRecordProcessor(dynamoDBClient, tableName);
    }
}
