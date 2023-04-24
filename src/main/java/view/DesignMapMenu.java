package view;

import controller.CommandParser;
import controller.Controller;
import controller.MapDesignController;

import java.util.HashMap;
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
        HashMap<String , String > options;
        String input;
        Matcher matcher;
        while (true) {
            input = scanner.nextLine();
            if ((options = commandParser.validate(input,"set texture",
                    "x|positionX/y|positionY/x1|positionX1/y1|positionY1/x2|positionX2/y2|positionY2/n|name")) != null){
                System.out.println(mapDesignController.setTexture(options));
            }else if(input.equals("start"))
                return "start";
            else
                System.out.println("invalid command");
        }

    }

}
