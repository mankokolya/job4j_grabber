package ru.job4j.grabber;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PsqlStore implements Store, AutoCloseable {
    private Connection cnn;

    public PsqlStore(Properties cfg) {
        try {
            Class.forName((cfg.getProperty("driver-class-name")));
            cnn = DriverManager.getConnection(
                    cfg.getProperty("url"),
                    cfg.getProperty("username"),
                    cfg.getProperty("password")
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void close() throws Exception {
        if (cnn != null) {
            cnn.close();
        }
    }

    @Override
    public void save(Post post) {
        String addPost = "INSERT INTO jobs.post (name, description, link, created) values (?, ?, ?, ?) ON CONFLICT (link) DO NOTHING";
        try (PreparedStatement statement = cnn.prepareStatement(addPost)) {
            statement.setString(1, post.getTitle());
            statement.setString(2, post.getDescription());
            statement.setString(3, post.getUrl());
            statement.setDate(4, Date.valueOf(post.getCreationTime()));
            statement.executeUpdate();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    @Override
    public List<Post> getAll() {
        List<Post> posts = new ArrayList<>();
        String all = "Select * from jobs.post";
        try (Statement statement = cnn.createStatement();
             ResultSet resultSet = statement.executeQuery(all)) {
            while (resultSet.next()) {
                posts.add(initializePost(resultSet));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return posts;
    }

    @Override
    public Post findById(String id) {
        Post post = new Post();
        String findById = "select * from jobs.post where id = ?";
        try (PreparedStatement statement = cnn.prepareStatement(findById)) {
            statement.setInt(1, Integer.parseInt(id));
            try (ResultSet resultSet = statement.executeQuery()) {
                post = initializePost(resultSet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    private static Post initializePost(ResultSet resultSet) throws Exception {
        if (resultSet.next()) {
            return new Post(resultSet.getString(2),
                    resultSet.getString(3),
                    resultSet.getString(4),
                    resultSet.getDate(5).toLocalDate()
            );
        }
        throw new IllegalArgumentException();
    }

}
