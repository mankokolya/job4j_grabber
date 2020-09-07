package ru.job4j.html;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
                Date dateTime = convertDateFromString(d.text());
                System.out.println(dateTime.toString());
                System.out.println();
            }
        }
    }

    public static Date convertDateFromString(String date) throws ParseException {
        Date rsl;
        final Map<String, String> correctMonthAbbreviations = twoListsToMap();
        final SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yy, HH:mm", new Locale("ru"));
        if (date.contains("сегодня") || date.contains("вчера")) {
            rsl = getTodayOrYesterdayDay(date, formatter);
        } else {
            String[] split = date.split(" ");
            split[1] = correctMonthAbbreviations.get(split[1]);
            rsl = formatter.parse(String.join(" ", split));
        }
        return rsl;
    }

    private static Date getTodayOrYesterdayDay(String date, SimpleDateFormat formatter) throws ParseException {
        Date rsl = null;
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yy", new Locale("ru"));
        final String[] splitDate = date.split(", ");
        if (date.contains("сегодня")) {
            rsl = formatter.parse(String.join(
                    ", ", dateFormat.format(calendar.getTime()), splitDate[1]));
        } else if (date.contains("вчера")) {
            calendar.add(Calendar.DATE, -1);
            rsl = formatter.parse(String.join(
                    ", ", dateFormat.format(calendar.getTime()), splitDate[1]));
        }
        return rsl;
    }

    private static Map<String, String> twoListsToMap() {
        List<String> wrongMonth = List.of(
                "янв", "фев", "мар", "апр", "май", "июн", "июл", "авг", "сен", "окт", "ноя", "дек"
        );
        List<String> correctMonth = List.of(
                "янв.", "февр.", "мар.", "апр.", "мая", "июн.", "июл.", "авг.", "сент.", "окт.", "нояб.", "дек.");
        return IntStream.range(0, wrongMonth.size()).boxed()
                .collect(Collectors.toMap(wrongMonth::get, correctMonth::get));
    }
}
