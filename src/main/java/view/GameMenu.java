package view;

import controller.CommandParser;
import controller.GameController;
import java.util.HashMap;

public class GameMenu {
    private final GameController gameController;
    private final CommandParser commandParser;
    public GameMenu (GameController gameController) {
        this.gameController = gameController;
        commandParser = new CommandParser();
    }
    public String run() {
        HashMap<String , String > options;
        String input;
        while (true) {
            input = CommandParser.getScanner().nextLine();
            if (commandParser.validate(input, "back", null) != null) return "back";
            if (commandParser.validate(input, "show current menu", null) != null)
                System.out.println("Game menu");
            else if (commandParser.validate(input, "trade menu", null) != null) {
                System.out.println("You enter trade menu successfully!"); return "trade";
            }
            else if (commandParser.validate(input, "shop menu", null) != null) {
                System.out.println("You enter shop menu successfully!"); return "shop";
            }
            else if (commandParser.validate(input, "building menu", null) != null) {
                System.out.println("You enter building menu successfully!"); return "building";
            }
            else if ((options = (commandParser.validate(input, "drop building","x|xPosition/y|yPosition/t|type"))) != null)
                System.out.println(gameController.dropBuilding(options));
            else if (commandParser.validate(input, "unit menu", null) != null) {
                System.out.println("You enter unit menu successfully!"); return "unit";
            }
            else if (commandParser.validate(input,"next turn", null) != null){
                System.out.println(gameController.nextTurn());
            }
            else System.out.println("invalid command");
        }
    }
}
