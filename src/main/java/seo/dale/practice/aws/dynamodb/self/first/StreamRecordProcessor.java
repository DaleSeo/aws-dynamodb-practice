package seo.dale.practice.aws.dynamodb.self.first;

import com.amazonaws.services.dynamodbv2.streamsadapter.model.RecordAdapter;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.InvalidStateException;
import com.amazonaws.services.kinesis.clientlibrary.exceptions.ShutdownException;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.IRecordProcessorCheckpointer;
import com.amazonaws.services.kinesis.clientlibrary.interfaces.v2.IRecordProcessor;
import com.amazonaws.services.kinesis.clientlibrary.lib.worker.ShutdownReason;
import com.amazonaws.services.kinesis.clientlibrary.types.InitializationInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ProcessRecordsInput;
import com.amazonaws.services.kinesis.clientlibrary.types.ShutdownInput;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class StreamRecordProcessor implements IRecordProcessor {
    private String shardId;

    @Override
    public void initialize(InitializationInput initializationInput) {
        log.debug("initialize... shardId: {}, ExtendedSequenceNumber: {}",
                initializationInput.getShardId(),
                initializationInput.getExtendedSequenceNumber()
        );
        shardId = initializationInput.getShardId();
    }

    @Override
    public void processRecords(ProcessRecordsInput processRecordsInput) {
        log.debug("processRecords... MillisBehindLatest: {}, RecordsSize: {}",
                processRecordsInput.getMillisBehindLatest(),
                processRecordsInput.getRecords().size()
        );

        processRecordsInput.getRecords()
                .stream()
                .filter(record -> record instanceof RecordAdapter)
                .map(record -> (RecordAdapter) record)
                .forEach(record -> {
                    System.out.println("## Event: " + record.getInternalObject().getEventName());
                    System.out.println("## Stream Record: " + record);
                });
        checkpoint(processRecordsInput.getCheckpointer());
    }

    @Override
    public void shutdown(ShutdownInput shutdownInput) {
        log.debug("shutdown... shutdownReason: {}, checkpointer: {}",
                shutdownInput.getShutdownReason(),
                shutdownInput.getCheckpointer()
        );

        if (shutdownInput.getShutdownReason() == ShutdownReason.TERMINATE) {
            checkpoint(shutdownInput.getCheckpointer());
        }
    }

    private void checkpoint(IRecordProcessorCheckpointer checkpointer) {
        log.debug("Checkpointing shard: {}", shardId);
        try {
            checkpointer.checkpoint();
        } catch (InvalidStateException | ShutdownException e) {
            e.printStackTrace();
        }
    }
}
