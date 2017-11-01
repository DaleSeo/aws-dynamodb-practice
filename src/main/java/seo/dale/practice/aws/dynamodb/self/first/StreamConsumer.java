package seo.dale.practice.aws.dynamodb.self.first;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.services.cloudwatch.AmazonCloudWatch;
import com.amazonaws.services.cloudwatch.AmazonCloudWatchClient;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.streamsadapter.AmazonDynamoDBStreamsAdapterClient;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessorFactory;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.InitialPositionInStream;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.KinesisClientLibConfiguration;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.Worker;

public class StreamConsumer {

    public void consume() {
        KinesisClientLibConfiguration kclConfig = new KinesisClientLibConfiguration(
                Config.APPLICATION_NAME,
                Config.STREAM_NAME,
                new ProfileCredentialsProvider(),
                Config.WORKER_NAME
        )
                .withRegionName(Config.REGION_NAME)
                .withInitialPositionInStream(InitialPositionInStream.TRIM_HORIZON);

        IRecordProcessorFactory recordProcessorFactory = () -> new StreamRecordProcessor();

        AmazonDynamoDBStreamsAdapterClient adapterClient = new AmazonDynamoDBStreamsAdapterClient(new ProfileCredentialsProvider(), new ClientConfiguration());
        adapterClient.setEndpoint("https://streams.dynamodb.us-west-2.amazonaws.com");

        AmazonDynamoDB dynamoDBClient = new AmazonDynamoDBClient();
        AmazonCloudWatch cloudWatchClient = new AmazonCloudWatchClient();

        Worker worker = new Worker.Builder()
                .config(kclConfig)
                .recordProcessorFactory(recordProcessorFactory)
                .kinesisClient(adapterClient)
                .dynamoDBClient(dynamoDBClient)
                .cloudWatchClient(cloudWatchClient)
                .build();

        new Thread(worker).start();
    }

    public static void main(String[] args) {
        new StreamConsumer().consume();
    }
}
