package com.activity.bookappmanager;

public class BookClass {
    private String bookTitle;
    private String bookDesc;
    private String bookAuthor;
    private String bookCover;
    private String key;

    public String getKey() {
        return key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookDesc() {
        return bookDesc;
    }

    public String getBookAuthor() {
        return bookAuthor;
    }

    public String getBookCover() {
        return bookCover;
    }

    public BookClass(String bookTitle, String bookDesc, String bookAuthor, String bookCover) {
        this.bookTitle = bookTitle;
        this.bookDesc = bookDesc;
        this.bookAuthor = bookAuthor;
        this.bookCover = bookCover;
    }

    public BookClass (){}
}
