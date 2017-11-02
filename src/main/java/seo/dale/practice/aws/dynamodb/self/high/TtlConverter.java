package seo.dale.practice.aws.dynamodb.self.high;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverter;

public class TtlConverter implements DynamoDBTypeConverter<Long, Long> {
    private static Long RETENTION_PERIOD_IN_SECOND = 60L;

    @Override
    public Long convert(Long object) {
        Long ttl = System.currentTimeMillis() / 1000 + RETENTION_PERIOD_IN_SECOND;
        return ttl;
    }

    @Override
    public Long unconvert(Long object) {
        return null;
    }
}
