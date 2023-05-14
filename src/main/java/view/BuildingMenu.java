package view;

import controller.BuildingController;
import controller.CommandParser;
import java.util.HashMap;

public class BuildingMenu {
    private final CommandParser commandParser;
    private final BuildingController buildingController;

    public BuildingMenu(BuildingController buildingController) {
        this.buildingController = buildingController;
        this.commandParser = new CommandParser();
    }

    public void mainBuildingClassRun() {
        String command, result;
        System.out.println(buildingController.buildingName());
        result = buildingController.redirect();
        if (result == null) {
            while (true) {
                command = CommandParser.getScanner().nextLine();
                if (commandParser.validate(command, "back", null) != null) return;
                else System.out.println("Invalid command");
            }
        }
    }

    public String defensiveBuildingRnu() {
        String command;
        HashMap<String, String> optionPass;
        System.out.println(buildingController.buildingHp());
        while (true) {
            command = CommandParser.getScanner().nextLine();
            if (commandParser.validate(command, "back", null) != null) return "back";
            if (commandParser.validate(command, "repair", null) != null)
                System.out.println(buildingController.repairBuilding());
            if ((optionPass = commandParser.validate(command, "gate", "a|access")) != null)
                System.out.println(buildingController.openAccess(optionPass));
            else System.out.println("Invalid command");
        }

    }

    public String campBuildingRnu() {
        HashMap<String, String> optionPass;
        String command;
        System.out.println(buildingController.buildingHp());
        while (true) {
            command = CommandParser.getScanner().nextLine();
            if (commandParser.validate(command, "back", null) != null) return "back";
            if (commandParser.validate(command, "repair", null) != null)
                System.out.println(buildingController.repairBuilding());
            else if ((optionPass = commandParser.validate(command, "create unit","t|type/c|count")) != null)
                System.out.println(buildingController.createUnit(optionPass));
            else System.out.println("Invalid command");
        }
    }

    public String stockBuildingRun() {
        String command;
        while (true) {
            command = CommandParser.getScanner().nextLine();
            if (commandParser.validate(command, "back", null) != null) return "back";
            if (commandParser.validate(command, "show stock content", null) != null)
                System.out.println(buildingController.showResources());
            else System.out.println("Invalid command");
        }
    }

    public String siegeRun() {
        HashMap<String, String> optionPass;
        String command;
        while (true) {
            command = CommandParser.getScanner().nextLine();
            if (commandParser.validate(command, "back", null) != null) return "back";
            if ((optionPass = commandParser.validate(command, "move siege", "x|xPosition/y|yPosition")) != null)
                System.out.println(buildingController.moveSiege(optionPass));
            else if ((optionPass = commandParser.validate(command, "attack", "x|positionX/y|positionY")) != null)
                System.out.println(buildingController.attackOnUnit(optionPass));
            else System.out.println("Invalid command");
        }
    }

    public String produceBuildingRun() {
        HashMap<String, String> optionPass;
        String command;
        while (true) {
            command = CommandParser.getScanner().nextLine();
            if (commandParser.validate(command, "back", null) != null) return "back";
            else if ((optionPass = commandParser.validate(command, "set mode","m|mode")) != null)
                System.out.println(buildingController.setMode(optionPass));
            else System.out.println("Invalid command");
        }
    }

    public String runShop() {
        HashMap<String, String> optionPass;
        String command, result;
        while (true) {
            command = CommandParser.getScanner().nextLine();
            if (commandParser.validate(command, "back", null) != null) return "back";
            if (commandParser.validate(command, "show price list", null) != null)
                System.out.println(buildingController.showPriceList());
            else if ((optionPass = commandParser.validate(command, "buy" ,"i|itemName/a|itemAmount")) != null) {
                    result =  buildingController.buyFromShop(optionPass);
                    if (!result.equals("done")) {System.out.println(result); continue;}
                    System.out.println("Please verify your purchase : ");
                    command = CommandParser.getScanner().nextLine();
                    if (commandParser.validate(command, "purchase verification", null) != null)
                        System.out.println(buildingController.verified(optionPass, 1, 1.0));
                    else System.out.println("Purchase did not complete successfully! Please try again.");
            }
            else if ((optionPass = commandParser.validate(command, "sell" , "i|itemName/a|itemAmount")) != null) {
                    result = buildingController.sellFromShop(optionPass);
                    if (!result.equals("done")) {System.out.println(result); continue;}
                    System.out.println("Please verify your sell : ");
                    command = CommandParser.getScanner().nextLine();
                    if (commandParser.validate(command, "sell verification", null) != null)
                        System.out.println(buildingController.verified(optionPass, -1, 0.8));
                    else System.out.println("Sell items did not complete successfully! Please try again.");
            }
            else System.out.println("Invalid Command");
        }

    }
}
