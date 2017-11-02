package seo.dale.practice.aws.dynamodb.self.high;

import com.amazonaws.services.dynamodbv2.datamodeling.*;
import lombok.Data;

import java.util.Date;

@DynamoDBTable(tableName = Item.TABLE_NAME)
@Data
public class Item {
    public static final String TABLE_NAME = "Tests";

    @DynamoDBHashKey
    private String id;

    @DynamoDBRangeKey
    private Date date;

    private Long ttl;

    private Currency currency;

    @DynamoDBTypeConverted(converter = TtlConverter.class)
    public Long getTtl() {
        return ttl;
    }

    @CurrencyFormat(separator = "#")
    public Currency getCurrency() {
        return currency;
    }
}
