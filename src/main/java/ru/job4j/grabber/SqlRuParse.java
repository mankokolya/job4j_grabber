package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class SqlRuParse implements Parse {
    public static void main(String[] args) {
        String url = "https://www.sql.ru/forum/job-offers/";
        SqlRuParse parser = new SqlRuParse();
        List<String> urls = getAllUrls(url);
        List<Post> allPosts = urls.stream()
                .map(parser::list)
                .flatMap(List::stream)
                .collect(Collectors.toList());

       for (Post post : allPosts) {
           System.out.println(post);
           System.out.println();
       }
    }

    @Override
    public List<Post> list(String link) {
        Document doc = null;
        try {
            doc = Jsoup.connect(link).get();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Elements row = Objects.requireNonNull(doc).select(".postslisttopic");

        return row.stream().map(element -> element.child(0))
                .map(child -> child.attr("href"))
                .map(this::detail)
                .filter(post -> !(post.getTitle().contains("Сообщения от модераторов")
                        || post.getTitle().contains("Правила форума") || post.getTitle().contains("Шпаргалки")))
                .collect(Collectors.toList());
    }

    @Override
    public Post detail(String link) {
        return new Post(link);
    }

    private static List<String> getAllUrls(String url, int firstPage, int lastPage) {
        List<String> urls = new ArrayList<>();
        for (int i = firstPage; i < lastPage; i++) {
            urls.add(url + i);
        }
        return urls;
    }
    private static List<String> getAllUrls(String url) {
        return List.of(url);
    }
}
