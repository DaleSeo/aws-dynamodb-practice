package seo.dale.practice.aws.dynamodb.high;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;

import java.util.Set;

@DynamoDBTable(tableName = "Thread")
public class Thread {
    @DynamoDBHashKey(attributeName = "ForumName")
    public String forumName;
    @DynamoDBRangeKey(attributeName = "Subject")
    public String subject;
    @DynamoDBAttribute(attributeName = "Message")
    public String message;
    @DynamoDBAttribute(attributeName = "LastPostedDateTime")
    public String lastPostedDateTime;
    @DynamoDBAttribute(attributeName = "LastPostedBy")
    public String lastPostedBy;
    @DynamoDBAttribute(attributeName = "Tags")
    public Set<String> tags;
    @DynamoDBAttribute(attributeName = "Answered")
    public int answered;
    @DynamoDBAttribute(attributeName = "Views")
    public int views;
    @DynamoDBAttribute(attributeName = "Replies")
    public int replies;

    public String getForumName() {
        return forumName;
    }

    public void setForumName(String forumName) {
        this.forumName = forumName;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLastPostedDateTime() {
        return lastPostedDateTime;
    }

    public void setLastPostedDateTime(String lastPostedDateTime) {
        this.lastPostedDateTime = lastPostedDateTime;
    }

    public String getLastPostedBy() {
        return lastPostedBy;
    }

    public void setLastPostedBy(String lastPostedBy) {
        this.lastPostedBy = lastPostedBy;
    }

    public Set<String> getTags() {
        return tags;
    }

    public void setTags(Set<String> tags) {
        this.tags = tags;
    }

    public int getAnswered() {
        return answered;
    }

    public void setAnswered(int answered) {
        this.answered = answered;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int getReplies() {
        return replies;
    }

    public void setReplies(int replies) {
        this.replies = replies;
    }

}
