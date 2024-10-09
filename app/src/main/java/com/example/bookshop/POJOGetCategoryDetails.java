package com.example.bookshop;

public class POJOGetCategoryDetails {

    //POJO  => Plain Old Java Object ,reusebility,pojo multiple data get and set
    String id,categoryImage,categoryName;

    public POJOGetCategoryDetails(String sid, String scategoryImage, String scategoryName) {
        this.id = sid;
        this.categoryImage = scategoryImage;
        this.categoryName = scategoryName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
