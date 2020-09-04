package ru.job4j.html;

import java.util.Date;

public class Post {
    private String author;
    private Date creationTime;
    private String description;

    public Post(String author, Date creationTime, String description) {
        this.author = author;
        this.creationTime = creationTime;
        this.description = description;
    }
}
