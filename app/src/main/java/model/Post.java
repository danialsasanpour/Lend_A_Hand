package model;

import java.time.LocalDate;
import java.util.UUID;

public class Post {

    private String postId;
    private String createdBy;
    private Category category;
    private String description;
    private String location;
    private LocalDate timeFrame;
    private double price;

    public Post(String createdBy, Category category, String description, String location, LocalDate timeFrame) {
        this.postId = UUID.randomUUID().toString();
        this.createdBy = createdBy;
        this.category = category;
        this.description = description;
        this.location = location;
        this.timeFrame = timeFrame;
    }

    public Post() {
        this.postId = UUID.randomUUID().toString();
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDate getTimeFrame() {
        return timeFrame;
    }

    public void setTimeFrame(LocalDate timeFrame) {
        this.timeFrame = timeFrame;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "Post{" +
                "postId='" + postId + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", category=" + category +
                ", description='" + description + '\'' +
                ", location=" + location +
                ", timeFrame=" + timeFrame +
                ", price=" + price +
                '}';
    }
}
