package com.niziolekp.model;

public class Chapter {
    private String chapterTitle;

    private String chapterContent;

    @Override
    public String toString() {
        return "Chapter{" +
                "chapterTitle='" + chapterTitle + '\'' +
                ", chapterContent='" + chapterContent + '\'' +
                '}';
    }

    public Chapter(String chapterTitle, String chapterContent) {
        this.chapterTitle = chapterTitle;

        this.chapterContent = chapterContent;
    }

    public Chapter() {
    }

    public String getChapterTitle() {
        return chapterTitle;
    }

    public void setChapterTitle(String chapterTitle) {
        this.chapterTitle = chapterTitle;
    }



    public String getChapterContent() {
        return chapterContent;
    }

    public void setChapterContent(String chapterContent) {
        this.chapterContent = chapterContent;
    }
}
