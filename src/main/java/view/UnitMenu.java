package view;

import controller.CommandParser;
import controller.Controller;
import controller.GameController;

import java.util.HashMap;

public class UnitMenu {
    private GameController gameController;
    private CommandParser commandParser;

    public UnitMenu(GameController gameController) {
        this.gameController = gameController;
        commandParser = new CommandParser();
    }

    public void run() {
        HashMap<String , String> options;
        String input;
        while (true) {
            input = CommandParser.getScanner().nextLine();
            if ((options = commandParser.validate(input, "select unit", "x|positionX/y|positionY/t|type/n|number")) != null)
                System.out.println(gameController.selectUnit(options));
            else if ((options = commandParser.validate(input, "move unit to", "x|positionX/y|positionY")) != null)
                System.out.println(gameController.moveUnit(options));
            else if((options = commandParser.validate(input, "set", "x|positionX/y|positionY/t|type/s|situation")) != null)
                System.out.println(gameController.setSituation(options));
            else if ((options = commandParser.validate(input, "attack", "x|positionX/y|positionY/t|type")) != null)
                System.out.println(gameController.attackOnUnit(options));
            else
                System.out.println("Invalid command");
        }
    }
}
