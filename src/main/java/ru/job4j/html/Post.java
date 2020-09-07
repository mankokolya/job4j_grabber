package ru.job4j.html;

import java.util.Date;

public class Post {
    private String author;
    private Date creationTime;
    private String description;
    private String title;
    private String url;

    public Post(String author, Date creationTime, String description, String title, String url) {
        this.author = author;
        this.creationTime = creationTime;
        this.description = description;
        this.title = title;
        this.url = url;
    }
}
