package view;

import controller.CommandParser;
import controller.GameController;
import java.util.HashMap;

public class BuildingMenu {
    private final CommandParser commandParser;
    private final GameController gameController;

    public BuildingMenu(GameController gameController) {
        this.gameController = gameController;
        this.commandParser = new CommandParser();
    }

    public void run() {
        HashMap<String, String> optionPass;
        String command ;
        while (true) {
            command = CommandParser.getScanner().nextLine();
            if (commandParser.validate(command,"back",null) != null) return;
            if (commandParser.validate(command,"show building info",null) != null)
                System.out.println(gameController.buildingInfo());
            else System.out.println("Invalid command!");
        }
    }
}
