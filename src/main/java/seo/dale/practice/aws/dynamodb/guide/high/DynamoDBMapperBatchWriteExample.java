package seo.dale.practice.aws.dynamodb.guide.high;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.SaveBehavior;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * http://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBMapper.BatchWriteExample.html
 */
public class DynamoDBMapperBatchWriteExample {
    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();

    public static void main(String[] args) {
        try {
            DynamoDBMapper mapper = new DynamoDBMapper(client);

            testBatchSave(mapper);
            testBatchDelete(mapper);
            testBatchWrite(mapper);

            System.out.println("Example complete!");

        } catch (Throwable t) {
            System.err.println("Error running the DynamoDBMapperBatchWriteExample: " + t);
            t.printStackTrace();
        }
    }

    private static void testBatchSave(DynamoDBMapper mapper) {
        Book book1 = new Book();
        book1.id = 901;
        book1.inPublication = true;
        book1.ISBN = "902-11-11-1111";
        book1.pageCount = 100;
        book1.price = 10;
        book1.productCategory = "Book";
        book1.title = "My book created in batch write";

        Book book2 = new Book();
        book2.id = 902;
        book2.inPublication = true;
        book2.ISBN = "902-11-12-1111";
        book2.pageCount = 200;
        book2.price = 20;
        book2.productCategory = "Book";
        book2.title = "My second book created in batch write";

        Book book3 = new Book();
        book3.id = 903;
        book3.inPublication = false;
        book3.ISBN = "902-11-13-1111";
        book3.pageCount = 300;
        book3.price = 25;
        book3.productCategory = "Book";
        book3.title = "My third book created in batch write";

        System.out.println("Adding three books to ProductCatalog table.");
        mapper.batchSave(Arrays.asList(book1, book2, book3));
    }

    private static void testBatchDelete(DynamoDBMapper mapper) {
        Book book1 = mapper.load(Book.class, 901);
        Book book2 = mapper.load(Book.class, 902);
        System.out.println("Deleting two books from the ProductCatalog table.");
        mapper.batchDelete(Arrays.asList(book1, book2));
    }

    private static void testBatchWrite(DynamoDBMapper mapper) {
        // Create Forum item to save
        Forum forumItem = new Forum();
        forumItem.name = "Item BatchWrite Forum";
        forumItem.threads = 0;
        forumItem.category = "Amazon Web Services";

        // Create Thread item to save
        Thread threadItem = new Thread();
        threadItem.forumName = "AmazonDynamoDB";
        threadItem.subject = "My sample question";
        threadItem.message = "BatchWrite message";
        List<String> tags = new ArrayList<>();
        tags.add("batch operations");
        tags.add("write");
        threadItem.tags = new HashSet<>(tags);

        // Load ProductCatalog item to delete
        Book book3 = mapper.load(Book.class, 903);

        List<Object> objectsToWrite = Arrays.asList(forumItem, threadItem);
        List<Book> objectsToDelete = Arrays.asList(book3);

        mapper.batchWrite(objectsToWrite, objectsToDelete, SaveBehavior.CLOBBER.config());
    }
}
