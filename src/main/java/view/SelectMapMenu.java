package view;

import controller.CommandParser;
import controller.Controller;
import controller.GameController;
import controller.MapDesignController;
import model.Map;

import java.util.Scanner;
import java.util.regex.Matcher;

public class SelectMapMenu {
    private Controller controller;
    private MapDesignController mapDesignController;
    private final CommandParser commandParser;

    public SelectMapMenu(MapDesignController controller) {
        this.mapDesignController = controller;
        commandParser = new CommandParser();
    }

    public String run(){
        System.out.println("here you can design your map. when your map is ready type \'start\'");
        Scanner scanner = CommandParser.getScanner();
        String input;
        Matcher matcher;
        while (true) {
            input = scanner.nextLine();
            if (commandParser.validate(input,"logout",null) != null) {
                System.out.println("User logged out successfully!");
                return"";
            }
            else
                System.out.println("invalid command");
        }

    }

}
