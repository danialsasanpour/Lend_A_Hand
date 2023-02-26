package model;

import java.util.UUID;

public class ReviewRating {

    private String reviewId;
    private String byUser;
    private String toUser;
    private String review;
    private float rating;

    public ReviewRating(String reviewId, String byUser, String toUser, String review, float rating) {
        this.reviewId = UUID.randomUUID().toString();
        this.byUser = byUser;
        this.toUser = toUser;
        this.review = review;
        this.rating = rating;
    }

    public ReviewRating(){
        this.reviewId = UUID.randomUUID().toString();
    }

    public String getReviewId() {
        return reviewId;
    }

    public void setReviewId(String reviewId) {
        this.reviewId = reviewId;
    }

    public String getByUser() {
        return byUser;
    }

    public void setByUser(String byUser) {
        this.byUser = byUser;
    }

    public String getToUser() {
        return toUser;
    }

    public void setToUser(String toUser) {
        this.toUser = toUser;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    @Override
    public String toString() {
        return "ReviewRating{" +
                "reviewId='" + reviewId + '\'' +
                ", byUser='" + byUser + '\'' +
                ", toUser='" + toUser + '\'' +
                ", review='" + review + '\'' +
                ", rating=" + rating +
                '}';
    }
}
