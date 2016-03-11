package ca.douglascollege.flamingdodos.realestate.utils;

public class CaseTools {
    private static final String CAMEL_TO_SNAKE_REGEX = "([a-z])([A-Z]+)";
    private static final String CAMEL_TO_SNAKE_REPLACEMENT = "$1_$2";

    public static String camelToSnake(String camelString) {
        return camelString.replaceAll(CAMEL_TO_SNAKE_REGEX, CAMEL_TO_SNAKE_REPLACEMENT).toLowerCase();
    }
}
