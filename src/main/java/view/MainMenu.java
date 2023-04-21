package view;

import controller.Controller;

import java.util.Scanner;
import java.util.regex.Matcher;

public class MainMenu {
    private Controller controller;

    public MainMenu(Controller controller) {
        this.controller = controller;
    }

    public String run() {
        Scanner scanner = new Scanner(System.in);
        //toDo define a universal scanner
        String input;
        Matcher matcher;
        while (true) {
            input = scanner.nextLine();
            if (input.equals("logout")) {
                System.out.println("User logged out successfully!");
                return "logout";
            } else if (input.matches("\\s*profile\\s+menu\\s*")) {
                System.out.println("Entered profile menu!");
                return "profile";
            } else if (input.matches("\\s*start\\s+game\\s*")) {
                return "selectMap";
            } else if (input.matches("\\s*trade\\s+menu\\s*")) {
                return "trade";
            } else if (input.equals("show current menu"))
                System.out.println("Main Menu");
            else
                System.out.println("invalid command");
        }
    }

}
