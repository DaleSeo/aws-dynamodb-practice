package seo.dale.practice.aws.dynamodb.self.first;

import java.util.concurrent.TimeUnit;

public class Utils {
    public static void sleepInSeconds(long seconds) {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(seconds));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
