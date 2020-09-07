package ru.job4j.grabber;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.text.ParseException;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Post {
    private String author;
    private Date creationTime;
    private String description;
    private String title;
    private final String url;

    public Post(String url) {
        this.url = url;
        initializePost(url);
    }

    private void initializePost(String url) {
        try {
            Document doc = Jsoup.connect(url).get();
            Element table = doc.select(".msgTable").first();
            this.title = table.getElementsByAttributeValue("class", "messageHeader").text();
            Elements author = table.getElementsByAttributeValue("class", "msgBody");
            this.author = author.first().text().split(" ")[0];
            this.description = author.last().text();
            this.creationTime = getTime(table);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Date getTime(Element table) throws ParseException {
        String dateAndTime = table.getElementsByClass("msgFooter").text();
        Pattern datePattern = Pattern.compile("\\d{2}\\s\\X[^\\s]*\\s\\d{2},\\s\\d{2}:\\d{2}");
        Pattern todayOrYesterday = Pattern.compile("\\X[^,]*,\\s\\d{2}:\\d{2}");
        Matcher matcher = datePattern.matcher(dateAndTime);
        Matcher matcher1 = todayOrYesterday.matcher(dateAndTime);
        String date = matcher.find() ? matcher.group() : matcher1.find() ? matcher1.group() : "";
        return CorrectDateAndTime.convertDateFromString(date);
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

    public String getTitle() {
        return title;
    }
}