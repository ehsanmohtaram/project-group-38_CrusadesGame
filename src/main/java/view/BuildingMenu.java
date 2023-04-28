package view;

import controller.BuildingController;
import controller.CommandParser;

import java.util.HashMap;

public class
BuildingMenu {
    private final BuildingController buildingController;
    private final CommandParser commandParser;

    public BuildingMenu(BuildingController buildingController) {
        this.buildingController = buildingController;
        this.commandParser = new CommandParser();
    }

    public void run() {
        HashMap<String, String> optionPass;
        String command ,result;
        while (true) {
            command = CommandParser.getScanner().nextLine();
            if (commandParser.validate(command,"back",null) != null) return;
            if (commandParser.validate(command,"show current menu",null) != null) System.out.println("Building Menu");

            else System.out.println("Invalid command!");
        }
    }
}
