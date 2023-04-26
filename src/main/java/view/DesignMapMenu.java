package view;

import controller.CommandParser;
import controller.Controller;
import controller.MapDesignController;
import java.util.HashMap;
import java.util.Scanner;

public class DesignMapMenu {
    private Controller controller;
    private final MapDesignController mapDesignController;
    private final CommandParser commandParser;

    public DesignMapMenu(MapDesignController controller) {
        this.mapDesignController = controller;
        commandParser = new CommandParser();
    }

    public String run(){
        System.out.println("here you can design your map. when your map is ready type 'start'");
        HashMap<String , String > options;
        String input;
        while (true) {
            input = CommandParser.getScanner().nextLine();
            if ((options = commandParser.validate(input,"set texture",
                    "x|positionX/y|positionY/x1|positionX1/y1|positionY1/x2|positionX2/y2|positionY2/t|type")) != null)
                System.out.println(mapDesignController.setTexture(options));
            else if ((options = commandParser.validate(input,"clear","x|positionX/y|positionY")) != null) {
                System.out.println(mapDesignController.clear(options));
            }else if ((options = commandParser.validate(input,"drop rock","x|positionX/y|positionY/d|direction")) != null) {
                System.out.println(mapDesignController.dropRock(options));
            }else if ((options = commandParser.validate(input,"drop tree","x|positionX/y|positionY/t|type")) != null) {
                System.out.println(mapDesignController.dropTree(options));
            }else if ((options = commandParser.validate(input,"add user","x|positionX/y|positionY/u|user/f|flag")) != null) {
                System.out.println(mapDesignController.addUserToMap(options));
            }else if ((options = commandParser.validate(input,"drop building","x|positionX/y|positionY/t|type")) != null) {
                System.out.println(mapDesignController.dropBuilding(options));
            }else if ((options = commandParser.validate(input,"drop unit","x|positionX/y|positionY/t|type")) != null) {
                System.out.println(mapDesignController.dropUnit(options));
            }else if(input.equals("start"))
                return "start";
            else
                System.out.println("invalid command");
        }

    }

}
