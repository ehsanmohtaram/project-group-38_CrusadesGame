package view;

import controller.CommandParser;
import controller.Controller;

import java.util.*;

public class MainMenu {
    private final Controller controller;
    private final CommandParser commandParser;

    public MainMenu(Controller controller) {
        this.controller = controller;
        commandParser = new CommandParser();
    }

    public String run() {
        HashMap<String , String> options;
        String input, result;
        while (true) {
            input = CommandParser.getScanner().nextLine();
            if (commandParser.validate(input,"logout",null) != null) {
                System.out.println("User logged out successfully!");
                if (Controller.currentUser.getLoggedIn()) Controller.currentUser.setLoggedIn(false);
                Controller.currentUser = null;
                return "logout";
            } else if (commandParser.validate(input,"profile menu",null) != null) {
                System.out.println("Entered profile menu!");
                return "profile";
            } else if ((options = commandParser.validate(input,"new map","x|width/y|height/n|name")) != null) {
                result = controller.createNewMap(options);
                System.out.println(result);
                if(result.equals("Map was created successfully!")) return "selectMap";
            } else if (commandParser.validate(input,"default map",null) != null) {
                System.out.println(controller.showDefaultMaps());
                System.out.println("Please select one of default maps : (after selection you can still modify the map)");
                while (true){
                    input = CommandParser.getScanner().nextLine();
                    if(input.equals("back")) break;
                    result = controller.selectDefaultMap(input);
                    System.out.println(result);
                    if(result.equals("Map was created successfully!")) return "selectMap";
                }
            } else if (commandParser.validate(input,"choose from my maps",null) != null) {
                result = controller.chooseFromMyMap();
                System.out.println(result);
                if (!result.equals("You do not have any map from past")) {
                    System.out.println("Please choose one of the following maps to play : ");
                    input = CommandParser.getScanner().nextLine();
                    result = controller.chooseNumber(input);
                    if (!result.equals("start")) System.out.println(result);
                    else return "previous map";
                }
            }
            else if (commandParser.validate(input,"show current menu",null) != null)
                System.out.println("Main Menu");
            else System.out.println("invalid command");
        }
    }

}
