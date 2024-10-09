package com.example.bookshop;

public class POJOCategoryWiseBook {
    String id,categoryname,shopname,bookRatting,booklanguage,bookimage,bookname, bookprice,bookoffer;

    public POJOCategoryWiseBook(String id, String categoryname,
                                String shopname, String booklanguage, String bookimage,
                                String bookname, String bookprice, String bookoffer) {
        this.id = id;
        this.categoryname = categoryname;
        this.shopname = shopname;
        this.booklanguage = booklanguage;
        this.bookimage = bookimage;
        this.bookname = bookname;
        this.bookprice = bookprice;
        this.bookoffer = bookoffer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryname() {
        return categoryname;
    }

    public void setCategoryname(String categoryname) {
        this.categoryname = categoryname;
    }

    public String getShopname() {
        return shopname;
    }

    public void setShopname(String shopname) {
        this.shopname = shopname;
    }

    public String getBooklanguage() {
        return booklanguage;
    }

    public void setBooklanguage(String booklanguage) {
        this.booklanguage = booklanguage;
    }

    public String getBookimage() {
        return bookimage;
    }

    public void setBookimage(String bookimage) {
        this.bookimage = bookimage;
    }

    public String getBookname() {
        return bookname;
    }

    public void setBookname(String bookname) {
        this.bookname = bookname;
    }

    public String getBookprice() {
        return bookprice;
    }

    public void setBookprice(String bookprice) {
        this.bookprice = bookprice;
    }

    public String getBookoffer() {
        return bookoffer;
    }

    public void setBookoffer(String bookoffer) {
        this.bookoffer = bookoffer;
    }
}
