package ru.job4j.grabber;

import java.time.LocalDate;

public class Post {
    private LocalDate creationTime;
    private String description;
    private String title;
    private String url;

    public Post(String title, String description, String url, LocalDate creationDate) {
        this.creationTime = creationDate;
        this.description = description;
        this.title = title;
        this.url = url;
    }

    public Post() {
    }

    public LocalDate getCreationTime() {
        return creationTime;
    }

    public String getDescription() {
        return description;
    }

    public String getUrl() {
        return url;
    }

    public String getTitle() {
        return title;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void setCreationTime(LocalDate creationTime) {
        this.creationTime = creationTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    @Override
    public String toString() {
        return "Post{" + System.lineSeparator()
                + "creationTime=" + creationTime + "', " + System.lineSeparator()
                + "description='" + description + "', " + System.lineSeparator()
                + "title='" + title + "', " + System.lineSeparator()
                + "url='" + url + "', " + System.lineSeparator()
                + '}';
    }
}