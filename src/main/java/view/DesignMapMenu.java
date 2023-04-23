package view;

import controller.CommandParser;
import controller.Controller;
import controller.MapDesignController;

import java.util.Scanner;
import java.util.regex.Matcher;

public class DesignMapMenu {
    private Controller controller;
    private MapDesignController mapDesignController;
    private final CommandParser commandParser;

    public DesignMapMenu(MapDesignController controller) {
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
            if (commandParser.validate(input,"set texture","x|width/y|height/n|name") != null) {
                System.out.println("User logged out successfully!");
                return"";
            }else if(input.equals("start"))
                return "start";
            else
                System.out.println("invalid command");
        }

    }

}
