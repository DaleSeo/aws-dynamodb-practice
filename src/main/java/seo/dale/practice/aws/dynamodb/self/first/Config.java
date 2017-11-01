package seo.dale.practice.aws.dynamodb.self.first;

import com.amazonaws.regions.Regions;

public class Config {
    public static final String TABLE_NAME = "Tests";
    public static final String APPLICATION_NAME = "TestShards";
    public static final String STREAM_NAME = "arn:aws:dynamodb:us-west-2:233081777157:table/Tests/stream/2017-10-25T16:33:08.580";
    public static final String WORKER_NAME = "TestWorker";
    public static final String REGION_NAME = Regions.US_WEST_2.getName();
}