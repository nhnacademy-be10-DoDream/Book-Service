package shop.dodream.book.util;

public class BookUtils {

    private BookUtils() {
        throw new IllegalArgumentException("Util Class");
    }

    public static String removeSpecialChars(String input){
        if (input == null) return null;
        return input.replaceAll("[^가-힣a-zA-Z0-9\\s\\.\\-\\'\\&\\+\\%,]", " ");
    }
}
