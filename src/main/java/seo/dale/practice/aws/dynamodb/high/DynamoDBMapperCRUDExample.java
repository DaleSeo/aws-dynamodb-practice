package seo.dale.practice.aws.dynamodb.high;

import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.*;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapperConfig.ConsistentReads;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * http://docs.aws.amazon.com/ko_kr/amazondynamodb/latest/developerguide/DynamoDBMapper.CRUDExample1.html
 */
public class DynamoDBMapperCRUDExample {
    static AmazonDynamoDB client = AmazonDynamoDBClientBuilder.standard().build();

    public static void main(String[] args) {
        testCRUDOperations();
        System.out.println("Example complete!");
    }

    @DynamoDBTable(tableName = "ProductCatalog")
    public static class CatalogItem {
        @DynamoDBHashKey(attributeName = "Id")
        private Integer id;
        @DynamoDBAttribute(attributeName = "Title")
        private String title;
        @DynamoDBAttribute(attributeName = "ISBN")
        private String ISBN;
        @DynamoDBAttribute(attributeName = "Authors")
        private Set<String> bookAuthors;

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getISBN() {
            return ISBN;
        }

        public void setISBN(String ISBN) {
            this.ISBN = ISBN;
        }

        public Set<String> getBookAuthors() {
            return bookAuthors;
        }

        public void setBookAuthors(Set<String> bookAuthors) {
            this.bookAuthors = bookAuthors;
        }

        @Override
        public String toString() {
            return "Book [ISBN=" + ISBN + ", bookAuthors=" + bookAuthors + ", id=" + id + ", title=" + title + "]";
        }
    }

    private static void testCRUDOperations() {
        CatalogItem item = new CatalogItem();
        item.setId(601);
        item.setTitle("Book 601");
        item.setISBN("611-1111111111");
        item.setBookAuthors(new HashSet<>(Arrays.asList("Author1", "Author2")));

        // Save the item (book).
        DynamoDBMapper mapper = new DynamoDBMapper(client);
        mapper.save(item);

        // Retrieve the item.
        CatalogItem itemRetrieved = mapper.load(CatalogItem.class, 601);
        System.out.println("Item retrieved:");
        System.out.println(itemRetrieved);

        // Update the item.
        itemRetrieved.setISBN("622-2222222222");
        itemRetrieved.setBookAuthors(new HashSet<>(Arrays.asList("Author1", "Author3")));
        mapper.save(itemRetrieved);
        itemRetrieved.setISBN("622-2222222222");
        itemRetrieved.setBookAuthors(new HashSet<String>(Arrays.asList("Author1", "Author3")));

        // Retrieve the updated item.
        CatalogItem updatedItem = mapper.load(CatalogItem.class, 601, ConsistentReads.CONSISTENT.config());
        System.out.println("Retrieved the previously updated item:");
        System.out.println(updatedItem);

        // Delete the item.
//        mapper.delete(updatedItem);
//
//        // Try to retrieve deleted item.
//        CatalogItem deletedItem = mapper.load(CatalogItem.class, updatedItem.getId(), ConsistentReads.CONSISTENT.config());
//        if (deletedItem == null) {
//            System.out.println("Done - Sample item is deleted.");
//        }
    }
}
