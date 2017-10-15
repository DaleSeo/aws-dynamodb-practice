package seo.dale.practice.aws.dynamodb.high;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.List;

@DynamoDBTable(tableName = "ProductCatalog")
public class Bicycle {
    @DynamoDBHashKey(attributeName = "Id")
    private int id;
    @DynamoDBAttribute(attributeName = "Title")
    private String title;
    @DynamoDBAttribute(attributeName = "Description")
    private String description;
    @DynamoDBAttribute(attributeName = "BicycleType")
    private String bicycleType;
    @DynamoDBAttribute(attributeName = "Brand")
    private String brand;
    @DynamoDBAttribute(attributeName = "Price")
    private int price;
    @DynamoDBAttribute(attributeName = "Color")
    private List<String> color;
    @DynamoDBAttribute(attributeName = "ProductCategory")
    private String productCategory;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBicycleType() {
        return bicycleType;
    }

    public void setBicycleType(String bicycleType) {
        this.bicycleType = bicycleType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public List<String> getColor() {
        return color;
    }

    public void setColor(List<String> color) {
        this.color = color;
    }

    public String getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(String productCategory) {
        this.productCategory = productCategory;
    }

    @Override
    public String toString() {
        return "Bicycle [Type=" + bicycleType + ", color=" + color + ", price=" + price + ", product category="
                + productCategory + ", id=" + id + ", title=" + title + "]";
    }

}
