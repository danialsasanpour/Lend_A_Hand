package model;

import java.util.UUID;

public class Category {

    private String categoryId;
    private String categoryName;

    public Category(String categoryName){
        this.categoryId = UUID.randomUUID().toString();
        this.categoryName = categoryName;
    }

    public Category(){
        this.categoryId = UUID.randomUUID().toString();
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "Category{" +
                "categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
