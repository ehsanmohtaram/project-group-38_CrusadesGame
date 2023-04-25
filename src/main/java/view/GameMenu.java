package view;

import controller.CommandParser;
import controller.Controller;
import controller.GameController;
import controller.MapDesignController;

import java.util.HashMap;
import java.util.Scanner;
import java.util.regex.Matcher;

public class GameMenu {
    private GameController gameController;
    private final CommandParser commandParser;
    public GameMenu (GameController gameController) {
        this.gameController = gameController;
        commandParser = new CommandParser();
    }
    public String run() {
        System.out.println("the game has been started! now you can play");
        Scanner scanner = CommandParser.getScanner();
        HashMap<String , String > options;
        String input;
        while (true) {
            input = scanner.nextLine();
            if (commandParser.validate(input,"next turn", null) != null){
                System.out.println(gameController.nextTurn());
            }
            else
                System.out.println("invalid command");
        }
    }
}
