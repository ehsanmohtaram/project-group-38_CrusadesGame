package view;

import controller.CommandParser;
import controller.Controller;

import java.util.HashMap;
import java.util.Scanner;

public class MainMenu {
    private final Controller controller;
    private final CommandParser commandParser;

    public MainMenu(Controller controller) {
        this.controller = controller;
        commandParser = new CommandParser();
    }

    public String run() {
        Scanner scanner = CommandParser.getScanner();
        HashMap<String , String> options;
        String input;
        while (true) {
            input = scanner.nextLine();
            if (commandParser.validate(input,"logout",null) != null) {
                System.out.println("User logged out successfully!");
                if (Controller.currentUser.getLoggedIn()) Controller.currentUser.setLoggedIn(false);
                Controller.currentUser = null;
                return "logout";
            } else if (commandParser.validate(input,"profile menu",null) != null) {
                System.out.println("Entered profile menu!");
                return "profile";
            } else if ((options = commandParser.validate(input,"new map","x|width/y|height/n|name")) != null) {
                String result = controller.createNewMap(options);
                System.out.println(result);
                if(result.equals("successful"))
                    return "selectMap";
            }else if (input.matches("\\s*trade\\s+menu\\s*")) {
                return "trade";
            } else if (commandParser.validate(input,"default map",null) != null) {
                System.out.println(controller.showDefaultMaps());
                System.out.println("please select one:(after selection you can still modify the map)");
                while (true){
                    input = scanner.nextLine();
                    if(input.equals("back"))
                        break;
                    String result = controller.selectDefaultMap(input);
                    System.out.println(result);
                    if(result.equals("successful"))
                        return "selectMap";
                }
            } else if (commandParser.validate(input,"show current menu",null) != null)
                System.out.println("Main Menu");
            else
                System.out.println("invalid command");
        }
    }

}
