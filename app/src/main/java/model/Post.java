package model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

public class Post implements Serializable {

    private String postId;
    private String createdBy;
    //private Category category;
    private String description;
    private String location;
    private String dateFrom;
    private String dateTo;
    private String timeFrom;
    private String timeTo;
    private double price;

    public Post(String createdBy, String description, String location,
                String dateFrom, String dateTo, String timeFrom, String timeTo) {
        this.postId = UUID.randomUUID().toString();
        this.createdBy = createdBy;
        this.description = description;
        this.location = location;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    public Post() {

    }

    public String getDateFrom() {
        return dateFrom;
    }

    public void setDateFrom(String dateFrom) {
        this.dateFrom = dateFrom;
    }

    public String getDateTo() {
        return dateTo;
    }

    public void setDateTo(String dateTo) {
        this.dateTo = dateTo;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
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
                ", description='" + description + '\'' +
                ", location=" + location +
                ", from=" + dateFrom +
                ", =" + timeFrom +
                ", to=" + dateTo +
                ", " + timeTo +
                ", price=" + price +
                '}';
    }

    public String getDateTimeFrom() {
        return "Date: " + getDateFrom() + ". Time: " + getTimeFrom();
    }

    public String getDateTimeTo() {
        return "Date: " + getDateTo() + ". Time: " + getTimeTo();
    }
}
