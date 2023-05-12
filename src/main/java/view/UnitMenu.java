package view;

import controller.CommandParser;
import controller.UnitController;

import java.util.HashMap;

public class UnitMenu {
    private final UnitController unitController;
    private final CommandParser commandParser;

    public UnitMenu(UnitController unitController) {
        this.unitController = unitController;
        commandParser = new CommandParser();
    }

    public void mainRun() {
        unitController.redirect();
    }

    public void runEngineer() {
        HashMap<String , String> options;
        String input;
        while (true) {
            input = CommandParser.getScanner().nextLine();
            if (commandParser.validate(input, "back", null) != null) return;
            if((options = commandParser.validate(input, "set", "s|situation")) != null)
                System.out.println(unitController.setSituation(options));
            else if ((options = commandParser.validate(input, "drop siege", "x|xPosition/y|yPosition/t|type")) != null)
                System.out.println(unitController.dropSiege(options));
            else if ((options = commandParser.validate(input, "disband", "x1/y1/x2/y2")) != null)
                System.out.println(unitController.disband());
            else System.out.println("Invalid command!");
        }
    }

    public void runWorker() {
        HashMap<String , String> options;
        String input;
        while (true) {
            input = CommandParser.getScanner().nextLine();
            if (commandParser.validate(input, "back", null) != null) return;
            if((options = commandParser.validate(input, "set", "s|situation")) != null)
                System.out.println(unitController.setSituation(options));
            else System.out.println("Invalid command!");
        }
    }

    public void runUnit() {
        HashMap<String , String> options;
        String input;
        while (true) {
            input = CommandParser.getScanner().nextLine();
            if (commandParser.validate(input, "back", null) != null) return;
            if(!unitController.checkRemainingUnits()) {
                System.out.println("the selected units are already dead or removed. you will automatically go to game menu");
                return;
            }
            if((options = commandParser.validate(input, "set", "s|situation")) != null)
                System.out.println(unitController.setSituation(options));
            else if ((options = commandParser.validate(input, "move unit to", "x|positionX/y|positionY")) != null)
                System.out.println(unitController.moveUnit(options));
            else if ((options = commandParser.validate(input, "attack", "x|positionX/y|positionY")) != null)
                System.out.println(unitController.attackOnUnit(options));
            else if ((options = commandParser.validate(input, "patrol unit", "x1/y1/x2/y2")) != null)
                System.out.println(unitController.patrolUnit(options));
            else if ((options = commandParser.validate(input, "disband", "x1/y1/x2/y2")) != null)
                System.out.println(unitController.disband());
            else System.out.println("Invalid command!");
        }
    }

}
