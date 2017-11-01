package seo.dale.practice.aws.dynamodb.self.first;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

@Slf4j
public class ItemPutter {

    private final DynamoDBMapper dbMapper;

    public ItemPutter(DynamoDBMapper dbMapper) {
        this.dbMapper = dbMapper;
    }

    public void put() {
        put(UUID.randomUUID().toString());
    }

    public void put(String id) {
        Item item = new Item();
        item.setId(id);
        dbMapper.save(item);
        log.info("inserted: {}", item);

        Utils.sleepInSeconds(1);

        item.setExpired(item.getCreated().getTime() / 1000 + 60);
        dbMapper.save(item);
        log.info("updated: {}", item);

        Utils.sleepInSeconds(1);
    }

    public static void main(String[] args) {
        AmazonDynamoDB dynamoDB = AmazonDynamoDBClientBuilder.defaultClient();
        DynamoDBMapper dbMapper = new DynamoDBMapper(dynamoDB);
        ItemPutter itemPutter = new ItemPutter(dbMapper);

        int counter = 0;
        while (counter++ < 100) {
            itemPutter.put(Integer.toString(counter));
        }
    }
}
