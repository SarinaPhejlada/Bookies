package com.example.bookies;

import java.io.Serializable;

import androidx.annotation.Nullable;

public class Book implements Serializable {

    private String title
                   ,author
                   ,ISBNNumber
                   ,imageLink
                   ,seller
                   ,review;

    //default constructor
    public Book() {
        this.title = "Untitled";
        this.author = "Unnamed";
        this.ISBNNumber = "Unregistered";
        this.imageLink = "No Image";
        this.seller = "Not specified";
        this.review = "No Review Found";
    }

    public Book(String title
            , String author
            , String ISBNNumber
            , String imageLink
            , String seller
            , String review) {
        this.title = title;
        this.author = author;
        this.ISBNNumber = ISBNNumber;
        this.imageLink = imageLink;
        this.seller = seller;
        this.review = review;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getISBNNumber() {
        return ISBNNumber;
    }

    public void setISBNNumber(String ISBNNumber) {
        this.ISBNNumber = ISBNNumber;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", ISBNNumber='" + ISBNNumber + '\'' +
                ", imageLink='" + imageLink + '\'' +
                ", seller='" + seller + '\'' +
                ", review='" + review + '\'' +
                '}';
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        Book book = (Book) obj;
        return ((Book) obj).ISBNNumber == this.ISBNNumber;
    }
}
