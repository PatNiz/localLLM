package com.niziolekp.model;

import java.util.List;

public class Book {
    private String title;
    private List<Chapter> chapterList;

    public Book(){

    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", chapterList=" + chapterList +
                '}';
    }

    public Book(String title, List<Chapter> chapterList) {
        this.title = title;
        this.chapterList = chapterList;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Chapter> getChapterList() {
        return chapterList;
    }

    public void setChapterList(List<Chapter> chapterList) {
        this.chapterList = chapterList;
    }
}
