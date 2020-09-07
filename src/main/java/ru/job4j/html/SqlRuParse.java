package ru.job4j.html;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import ru.job4j.grabber.CorrectDateAndTime;

import java.util.*;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        List<String> urls = getAllUrls();
        for (String url : urls) {
            Document doc = Jsoup.connect(url).get();
            Elements row = doc.select(".postslisttopic");
            Elements date = doc.select(".altCol");
            Iterator<Element> rowIterator = row.iterator();
            Iterator<Element> dateIterator = date.iterator();
            printValues(rowIterator, dateIterator);
        }
    }

    private static List<String> getAllUrls() {
        List<String> urls = new ArrayList<>();
        int firstPage = 1;
        int lastPage = 6;
        String url = "https://www.sql.ru/forum/job-offers/";
        for (int i = firstPage; i < lastPage; i++) {
            urls.add(url + i);
        }
        return urls;
    }

    private static void printValues(Iterator<Element> rowIterator, Iterator<Element> dateIterator) throws
            ParseException {
        while (rowIterator.hasNext()) {
            Element el = rowIterator.next();
            Element href = el.child((0));
            System.out.println(href.attr("href"));
            System.out.println(href.text());
            if (dateIterator.hasNext()) {
                dateIterator.next();
                Element d = dateIterator.next();
                Date dateTime = CorrectDateAndTime.convertDateFromString(d.text());
                System.out.println(dateTime.toString());
                System.out.println();
            }
        }
    }
}
