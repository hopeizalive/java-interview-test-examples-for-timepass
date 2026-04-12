package com.example.springdata.interview.sddata.l12;

/** Class-based read model via JPQL {@code select new ...}. */
public class Sd12NovelDto {

    private final String title;
    private final String author;

    public Sd12NovelDto(String title, String author) {
        this.title = title;
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return title + " by " + author;
    }
}
