package ru.job4j.grabber;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class CorrectDateAndTime {
    public static LocalDate convertDateFromString(String date) {
        LocalDate rsl;
        final Map<String, String> correctMonthAbbreviations = twoListsToMap();
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMM yy", new Locale("ru"));

        if (date.contains("сегодня") || date.contains("вчера")) {
            rsl = getTodayOrYesterdayDay(date);
        } else {
            String[] dateOnly = date.split(", ");
            String[] correctDate = dateOnly[0].split(" ");
            correctDate[1] = correctMonthAbbreviations.get(correctDate[1]);
            rsl = LocalDate.parse(String.join(" ", correctDate), formatter);
        }
        return rsl;
    }

    private static LocalDate getTodayOrYesterdayDay(String date) {
        LocalDate today = LocalDate.now();
        return date.contains("сегодня") ? today : today.minusDays(1);
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
