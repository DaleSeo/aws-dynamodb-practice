package gsi;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBScanExpression;
import com.amazonaws.services.dynamodbv2.datamodeling.PaginatedScanList;
import com.amazonaws.services.dynamodbv2.datamodeling.ScanResultPage;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.ComparisonOperator;
import com.amazonaws.services.dynamodbv2.model.Condition;
import org.junit.Test;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public class GsiScanTest {
    private static AWSCredentialsProvider awsCredentialsProvider =
            new AWSStaticCredentialsProvider(new BasicAWSCredentials(Idempotence.ACCESS_KEY, Idempotence.SECRET_KEY));
    private static AmazonDynamoDB client = AmazonDynamoDBClientBuilder
            .standard()
            .withCredentials(awsCredentialsProvider)
            .withRegion(Regions.US_EAST_1)
            .build();
    private static DynamoDBMapper mapper = new DynamoDBMapper(client);

    @Test
    public void testLoad() {
        Idempotence idempotence = mapper.load(Idempotence.class, "FuncTest.da9cc098-0e73-4487-80f1-4d55c8f84e31.93929c6d-5ba3-43a8-9fd7-05608c3ab965", 0L);
        System.out.println(idempotence);
    }

    @Test
    public void testScanGsi() {
        String lookbackTimestamp = ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(7)
                .format(DateTimeFormatter.ISO_DATE_TIME);
        System.out.println("#lookback: " + lookbackTimestamp);
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withLimit(10)
                .withIndexName(Idempotence.GSI_NAME)
                .withFilterConditionEntry(
                        Idempotence.BOOKING_DATETIME_NAME,
                        new Condition()
                                .withComparisonOperator(ComparisonOperator.LT)
                                .withAttributeValueList(new AttributeValue().withS(lookbackTimestamp))
                );
        PaginatedScanList<Idempotence> scanList = mapper.scan(Idempotence.class, scanExpression);
        StreamSupport.stream(scanList.spliterator(), false)
                .forEach(idem -> {
                    System.out.println(idem);
                });

        System.out.println("#size: " + scanList.size());
    }

    @Test
    public void testScanGsi2() {
        DynamoDBScanExpression scanExpression = new DynamoDBScanExpression()
                .withLimit(1000)
                .withExclusiveStartKey(null)
                .withIndexName(Idempotence.GSI_NAME);

        do {
            String lookbackTimestamp = ZonedDateTime.now(ZoneOffset.UTC).minusMinutes(7)
                    .format(DateTimeFormatter.ISO_DATE_TIME);
            System.out.println("#lookback: " + lookbackTimestamp);

            scanExpression.withFilterConditionEntry(
                    Idempotence.BOOKING_DATETIME_NAME,
                    new Condition()
                            .withComparisonOperator(ComparisonOperator.LT)
                            .withAttributeValueList(new AttributeValue().withS(lookbackTimestamp))
            );

            ScanResultPage<Idempotence> scanPage = mapper.scanPage(Idempotence.class, scanExpression);
            scanExpression.setExclusiveStartKey(scanPage.getLastEvaluatedKey());

            scanPage.getResults().forEach(System.out::println);
        } while (scanExpression.getExclusiveStartKey() != null);
    }
}
