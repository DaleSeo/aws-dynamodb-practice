package gsi;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBIndexRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTypeConverted;
import lombok.Data;

import java.time.ZonedDateTime;
import java.util.UUID;

@Data
@DynamoDBTable(tableName = Idempotence.TABLE_NAME)
public class Idempotence {
    public static final String ACCESS_KEY = "";
    public static final String SECRET_KEY = "";
    public static final String TABLE_NAME = "usl-idempotence-pie-usamazon";
    public static final String GSI_NAME = "bookingDateTime-isIndexed-index";
    public static final String BOOKING_DATETIME_NAME = "bookingDateTime";

    private String idempotenceId;

    private UUID subledgerEntryId;

    private String intent;

    private String recordStatus;

    private String isIndexed;

    private ZonedDateTime bookingDateTime;

    private ZonedDateTime globalWriteHorizon;

    private ZonedDateTime lastUpdated;

    private Long businessEventVersion;

    private Long idempotenceVersion;

    private Long adjustmentId;

    @DynamoDBHashKey
    public String getIdempotenceId()
    {
        return idempotenceId;
    }

    public void setIdempotenceId(
        final String idempotenceId)
    {
        this.idempotenceId = idempotenceId;
    }

    @DynamoDBRangeKey
    public Long getIdempotenceVersion()
    {
        return idempotenceVersion;
    }

    public void setIdempotenceVersion(
        final Long idempotenceVersion)
    {
        this.idempotenceVersion = idempotenceVersion;
    }

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = UuidConverter.class)
    public UUID getSubledgerEntryId()
    {
        return subledgerEntryId;
    }

    public void setSubledgerEntryId(
        final UUID subledgerEntryId)
    {
        this.subledgerEntryId = subledgerEntryId;
    }

    @DynamoDBAttribute
    public String getIntent()
    {
        return intent;
    }

    public void setIntent(
        final String intent)
    {
        this.intent = intent;
    }

    @DynamoDBAttribute
    public String getRecordStatus()
    {
        return recordStatus;
    }

    public void setRecordStatus(
        final String recordStatus)
    {
        this.recordStatus = recordStatus;
    }

    @DynamoDBAttribute
    public Long getBusinessEventVersion()
    {
        return businessEventVersion;
    }

    public void setBusinessEventVersion(
        final Long businessEventVersion)
    {
        this.businessEventVersion = businessEventVersion;
    }

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = ZonedDateTimeConverter.class)
    public ZonedDateTime getLastUpdated()
    {
        return lastUpdated;
    }

    public void setLastUpdated(
        final ZonedDateTime lastUpdated)
    {
        this.lastUpdated = lastUpdated;
    }

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = ZonedDateTimeConverter.class)
    @DynamoDBIndexHashKey(globalSecondaryIndexName = GSI_NAME)
    public ZonedDateTime getBookingDateTime()
    {
        return bookingDateTime;
    }

    public void setBookingDateTime(
        final ZonedDateTime bookingDateTime)
    {
        this.bookingDateTime = bookingDateTime;
    }

    @DynamoDBAttribute
    @DynamoDBTypeConverted(converter = ZonedDateTimeConverter.class)
    public ZonedDateTime getGlobalWriteHorizon()
    {
        return globalWriteHorizon;
    }

    public void setGlobalWriteHorizon(
        final ZonedDateTime globalWriteHorizon)
    {
        this.globalWriteHorizon = globalWriteHorizon;
    }

    @DynamoDBAttribute
    public Long getAdjustmentId()
    {
        return adjustmentId;
    }

    public void setAdjustmentId(
        final Long adjustmentId)
    {
        this.adjustmentId = adjustmentId;
    }

    @DynamoDBAttribute
    @DynamoDBIndexRangeKey(globalSecondaryIndexName = GSI_NAME)
    public String getIsIndexed()
    {
        return isIndexed;
    }

    public void setIsIndexed(
        final String isIndexed)
    {
        this.isIndexed = isIndexed;
    }
}
