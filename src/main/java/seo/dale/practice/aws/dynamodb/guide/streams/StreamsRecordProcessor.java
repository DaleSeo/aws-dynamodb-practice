package seo.dale.practice.aws.dynamodb.guide.streams;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.streamsadapter.model.RecordAdapter;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.ShutdownReason;
import com.amazonaws.services.kinesis.model.Record;

import java.nio.charset.StandardCharsets;
import java.util.List;

public class StreamsRecordProcessor implements IRecordProcessor {
    private Integer checkpointCounter;

    private final AmazonDynamoDB dynamoDBClient;
    private final String tableName;

    public StreamsRecordProcessor(AmazonDynamoDB dynamoDBClient, String tableName) {
        this.dynamoDBClient = dynamoDBClient;
        this.tableName = tableName;
    }

    @Override
    public void initialize(String shardId) {
        checkpointCounter = 0;
    }

    @Override
    public void processRecords(List<Record> records, IRecordProcessorCheckpointer checkpointer) {
        for (Record record : records) {
            String data = new String(record.getData().array(), StandardCharsets.UTF_8);
            System.out.println(data);
            if (record instanceof RecordAdapter) {
                com.amazonaws.services.dynamodbv2.model.Record streamRecord = ((RecordAdapter) record).getInternalObject();

                switch (streamRecord.getEventName()) {
                    case "MODIFY":
                        StreamsAdapterDemoHelper.putItem(dynamoDBClient, tableName, streamRecord.getDynamodb().getNewImage());
                        break;
                    case "REMOVE":
                        StreamsAdapterDemoHelper.deleteItem(dynamoDBClient, tableName, streamRecord.getDynamodb().getKeys().get("Id").getN());
                        break;
                }
            }
            checkpointCounter += 1;
            if (checkpointCounter % 10 == 0) {
                try {
                    checkpointer.checkpoint();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void shutdown(IRecordProcessorCheckpointer checkpointer, ShutdownReason reason) {
        if (reason == ShutdownReason.TERMINATE) {
            try {
                checkpointer.checkpoint();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}