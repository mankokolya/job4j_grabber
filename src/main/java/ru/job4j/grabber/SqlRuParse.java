package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class SqlRuParse implements Parse {
    private final List<String> url;
    private final int startPage = 0;
    private final int endPage = 0;

//    public static void main(String[] args) {
//        String url = "https://www.sql.ru/forum/job-offers/";
//        SqlRuParse parser = new SqlRuParse(url);
//        List<Post> allPosts = parser.url.stream()
//                .map(parser::list)
//                .flatMap(List::stream)
//                .collect(Collectors.toList());
//
//       for (Post post : allPosts) {
//           System.out.println(post);
//           System.out.println();
//       }
//    }
    SqlRuParse(String url) {
        this.url = getAllUrls(url);
    }

    SqlRuParse(String url, int startPage, int endPage) {
        this.url = getAllUrls(url, startPage, endPage);
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
                .skip(3)
                .map(this::detail)
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
            post.setDescription(author.last().text());
            post.setCreationTime(getTime(table));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return post;
    }

    private static LocalDate getTime(Element table) {
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

    public List<String> getUrl() {
        return url;
    }
}
