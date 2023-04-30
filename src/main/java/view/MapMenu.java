package view;

import controller.CommandParser;
import controller.Controller;
import controller.MapController;

import java.util.HashMap;

public class MapMenu {
    private MapController controller;
    private CommandParser commandParser;

    public MapMenu (MapController controller){
        this.controller = controller;
        commandParser = new  CommandParser();
    }
    public void run (){
        HashMap<String , String> options;
        String input, result;
        while (true) {
            input = CommandParser.getScanner().nextLine();
            if ((options = commandParser.validate(input,"move", "u|up/d|down/l|left/r|right")) != null)
                System.out.println(controller.moveMap(options));
            else if ((options = commandParser.validate(input,"show details","x|positionX/y|positionY")) != null)
                System.out.println(controller.showDetails(options));
            else if (commandParser.validate(input,"show current menu",null) != null)
                System.out.println("map Menu");
            else if (commandParser.validate(input,"exit",null) != null)
                return;
            else
                System.out.println("invalid command");
        }
    }
}
