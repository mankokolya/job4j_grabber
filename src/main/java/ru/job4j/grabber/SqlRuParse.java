package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    public Post detail(String url) {
        Post post = new Post();
        try {
            post.setUrl(url);
            Document doc = Jsoup.connect(url).get();
            Element table = doc.select(".msgTable").first();
            post.setTitle(table.getElementsByAttributeValue("class", "messageHeader").text());
            Elements author = table.getElementsByAttributeValue("class", "msgBody");
            post.setAuthor(author.first().text().split(" ")[0]);
            post.setDescription(author.last().text());
            post.setCreationTime(getTime(table));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    private static Date getTime(Element table) throws ParseException {
        String dateAndTime = table.getElementsByClass("msgFooter").text();
        Pattern datePattern = Pattern.compile("\\d{2}\\s\\X[^\\s]*\\s\\d{2},\\s\\d{2}:\\d{2}");
        Pattern todayOrYesterday = Pattern.compile("\\X[^,]*,\\s\\d{2}:\\d{2}");
        Matcher matcher = datePattern.matcher(dateAndTime);
        Matcher matcher1 = todayOrYesterday.matcher(dateAndTime);
        String date = matcher.find() ? matcher.group() : matcher1.find() ? matcher1.group() : "";
        return CorrectDateAndTime.convertDateFromString(date);
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
