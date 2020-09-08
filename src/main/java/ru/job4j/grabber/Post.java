package ru.job4j.grabber;

import java.util.Date;

public class Post {
    private String author;
    private Date creationTime;
    private String description;
    private String title;
    private String url;

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setCreationTime(Date creationTime) {
        this.creationTime = creationTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return "Post{" + System.lineSeparator()
                + "author='" + author + "', " + System.lineSeparator()
                + "creationTime=" + creationTime + "', " + System.lineSeparator()
                + "description='" + description + "', " + System.lineSeparator()
                + "title='" + title + "', " + System.lineSeparator()
                + "url='" + url + "', " + System.lineSeparator()
                + '}';
    }
}