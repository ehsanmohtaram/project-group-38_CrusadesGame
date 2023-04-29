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
        System.out.println("You can design your map here. When your map is ready input 'start game'");
        HashMap<String , String > options;
        String input, result;
        while (true) {
            input = CommandParser.getScanner().nextLine();
            if ((options = commandParser.validate(input,"move", "u|up/d|down/l|left/r|right")) != null) {
                result = controller.moveMap(options);
                if(!result.equals("successful"))System.out.println(controller.moveMap(options));
            }
            else if ((options = commandParser.validate(input,"clear","x|positionX/y|positionY")) != null)
                System.out.println(controller.showDetails(options));
            else
                System.out.println("invalid command");
        }
    }
}
