package seo.dale.practice.aws.dynamodb.guide.streams;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.DescribeTableResult;
import com.amazonaws.services.dynamodbv2.streamsadapter.AmazonDynamoDBStreamsAdapterClient;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;

/**
 * http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Streams.KCLAdapter.Walkthrough.CompleteProgram.html
 */
public class StreamsAdapterDemo {
    private static Worker worker;
    private static KinesisClientLibConfiguration workerConfig;
    private static IRecordProcessorFactory recordProcessorFactory;

    private static AmazonDynamoDBStreamsAdapterClient adapterClient;
    private static AmazonDynamoDB dynamoDBClient;

    private static AmazonCloudWatch cloudWatchClient;

    private static String tablePrefix = "KCL-Demo";
    private static String streamArn;

    public static void main(String[] args) throws Exception {
        System.out.println("Starting demo...");

        String srcTable = tablePrefix + "-src";
        String destTable = tablePrefix + "-dest";

        recordProcessorFactory = new StreamsRecordProcessorFactory(destTable);

        adapterClient = new AmazonDynamoDBStreamsAdapterClient(new ProfileCredentialsProvider(), new ClientConfiguration());
        adapterClient.setEndpoint("https://streams.dynamodb.us-west-2.amazonaws.com");
        dynamoDBClient = AmazonDynamoDBClientBuilder.standard().build();
        cloudWatchClient = AmazonCloudWatchClientBuilder.standard().build();
        
        setUpTables(srcTable, destTable);

        workerConfig = new KinesisClientLibConfiguration("streams-adapter-demo", streamArn, new ProfileCredentialsProvider(), "streams-demo-worker")
                .withMaxRecords(1000)
                .withIdleTimeBetweenReadsInMillis(500)
                .withInitialPositionInStream(InitialPositionInStream.TRIM_HORIZON);

        System.out.println("Creating worker for stream: " + streamArn);
        worker = new Worker(recordProcessorFactory, workerConfig, adapterClient, dynamoDBClient, cloudWatchClient);
        System.out.println("Starting worker...");

        Thread t = new Thread(worker);
        t.start();

        Thread.sleep(25000);
        worker.shutdown();
        t.join();

        if (StreamsAdapterDemoHelper.scanTable(dynamoDBClient, srcTable).getItems()
                .equals(StreamsAdapterDemoHelper.scanTable(dynamoDBClient, destTable).getItems())) {
            System.out.println("Scan result is equal.");
        } else {
            System.out.println("Tables are different!");
        }

        System.out.println("Done.");
        cleanupAndExit(0);
    }

    private static void setUpTables(String srcTable, String destTable) {
        streamArn = StreamsAdapterDemoHelper.createTable(dynamoDBClient, srcTable);
        StreamsAdapterDemoHelper.createTable(dynamoDBClient, destTable);

        awaitTableCreation(srcTable);

        performOps(srcTable);
    }

    private static void awaitTableCreation(String tableName) {
        Integer retries = 0;
        Boolean created = false;
        while (!created && retries < 100) {
            DescribeTableResult result = StreamsAdapterDemoHelper.describeTable(dynamoDBClient, tableName);
            created = result.getTable().getTableStatus().equals("ACTIVE");
            if (created) {
                System.out.println("Table is active.");
                return;
            }
            else {
                retries++;
                try {
                    Thread.sleep(1000);
                }
                catch (InterruptedException e) {
                    // do nothing
                }
            }
        }
        System.out.println("Timeout after table creation. Exiting...");
        cleanupAndExit(1);
    }

    private static void cleanupAndExit(Integer returnValue) {
        String srcTable = tablePrefix + "-src";
        String destTable = tablePrefix + "-dest";
        dynamoDBClient.deleteTable(new DeleteTableRequest().withTableName(srcTable));
        dynamoDBClient.deleteTable(new DeleteTableRequest().withTableName(destTable));
        System.exit(returnValue);
    }


    private static void performOps(String tableName) {
        StreamsAdapterDemoHelper.putItem(dynamoDBClient, tableName, "101", "test1");
        StreamsAdapterDemoHelper.updateItem(dynamoDBClient, tableName, "101", "test2");
        StreamsAdapterDemoHelper.deleteItem(dynamoDBClient, tableName, "101");
        StreamsAdapterDemoHelper.putItem(dynamoDBClient, tableName, "102", "demo3");
        StreamsAdapterDemoHelper.updateItem(dynamoDBClient, tableName, "102", "demo4");
        StreamsAdapterDemoHelper.deleteItem(dynamoDBClient, tableName, "102");
    }
}
