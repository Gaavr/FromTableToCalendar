package org.gaavr.google;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DateExtractor {

    public static void main(String[] args) {
        String tableData = "Ваша таблица с данными"; // Замените на реальные данные таблицы

        List<String> datesList = extractDatesFromTable(tableData);

        // Вывод списка дат
        for (String date : datesList) {
            System.out.println(date);
        }
    }

    public static List<String> extractDatesFromTable(String tableData) {
        List<String> datesList = new ArrayList<>();

        // Шаблон для поиска дат в формате "дд.мм.гггг" или "дд.мм.ггггг"
        Pattern datePattern = Pattern.compile("(\\d{2}\\.\\d{2}\\.\\d{4}|\\d{2}\\.\\d{2}\\.\\d{2})г\\.");

        // Проходим по всей таблице и ищем даты
        Matcher matcher = datePattern.matcher(tableData);
        while (matcher.find()) {
            datesList.add(matcher.group());
        }

        return datesList;
    }
}

