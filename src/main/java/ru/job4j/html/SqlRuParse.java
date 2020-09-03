package ru.job4j.html;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Iterator;

public class SqlRuParse {
    public static void main(String[] args) throws Exception {
        Document doc = Jsoup.connect("https://www.sql.ru/forum/job-offers").get();

        Elements row = doc.select(".postslisttopic");

        Elements date = doc.select(".altCol");
        Iterator<Element> rowIterator = row.iterator();
        Iterator<Element> dateIterator = date.iterator();
        while (rowIterator.hasNext()) {
            Element el = rowIterator.next();
            Element href = el.child((0));
            System.out.println(href.attr("href"));
            System.out.println(href.text());
            if (dateIterator.hasNext()) {
                dateIterator.next();
                Element d = dateIterator.next();
                System.out.println(d.text());
                System.out.println();
            }
        }
    }
}
