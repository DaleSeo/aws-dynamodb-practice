package seo.dale.practice.aws.dynamodb.self.high;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;

import java.util.Date;
import java.util.UUID;

public class ItemMain {
    private static AmazonDynamoDB amazonDynamoDB = AmazonDynamoDBClientBuilder.defaultClient();

    public static void main(String[] args) {
        DynamoDBMapper dbMapper = new DynamoDBMapper(amazonDynamoDB);

        Item item1 = new Item();
        item1.setId(UUID.randomUUID().toString());
        item1.setDate(new Date());
        item1.setTtl(System.currentTimeMillis() / 1000 + 60);
        Currency currency = new Currency();
        currency.setAmount(100.23);
        currency.setUnit("won");
        item1.setCurrency(currency);

        dbMapper.save(item1);

//        Item item2 = new Item();
//        item2.setId(UUID.randomUUID().toString());
//        item2.setDate(new Date());
//        item2.setTtl(null);
//
//        dbMapper.save(item2);
//
//        Item item3 = new Item();
//        item3.setId(UUID.randomUUID().toString());
//        item3.setDate(new Date());
//        item3.setTtl(0L);
//
//        dbMapper.save(item3);
    }
}
