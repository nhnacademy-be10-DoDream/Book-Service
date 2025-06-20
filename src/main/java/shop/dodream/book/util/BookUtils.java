package shop.dodream.book.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class BookUtils {
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");

    public static String removeSpecialChars(String input){
        if (input == null) return null;
        return input.replaceAll("[^가-힣a-zA-Z0-9\\s\\.\\-\\'\\&\\+\\%,]", " ");
    }

    public static LocalDate parseDate(String dateStr){
        if (dateStr == null || dateStr.isBlank()) return null;
        return LocalDate.parse(dateStr, DATE_TIME_FORMATTER);
    }
}
