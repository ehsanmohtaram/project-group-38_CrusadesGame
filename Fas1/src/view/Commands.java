package view;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum Commands {
    ;
    private final String regex;
    private static final Scanner scanner = new Scanner(System.in);

    Commands(String regex) {
        this.regex = regex;
    }

    public static Matcher getMatcher(String input, Commands command) {
        Matcher matcher = Pattern.compile(command.regex).matcher(input);
        return matcher.matches() ? matcher : null;
    }

    public static Scanner getScanner() {
        return scanner;
    }
}
