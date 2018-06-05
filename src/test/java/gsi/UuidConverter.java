package gsi;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

import java.util.UUID;

public class UuidConverter implements DynamoDBTypeConverter<String, UUID> {
    @Override
    public String convert(UUID uuid) {
        return uuid.toString();
    }

    @Override
    public UUID unconvert(String text) {
        return UUID.fromString(text);
    }
}
