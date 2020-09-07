package ru.job4j.grabber;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CorrectDateAndTime {
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
