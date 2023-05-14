package view;

import controller.CommandParser;
import controller.GameController;
import java.util.HashMap;

public class GameMenu {
    private final GameController gameController;
    private final CommandParser commandParser;

    public GameMenu(GameController gameController) {
        this.gameController = gameController;
        commandParser = new CommandParser();
    }

    public String run() {
        HashMap<String, String> options;
        String input, result;
        while (true) {
            input = CommandParser.getScanner().nextLine();
            if (commandParser.validate(input, "back", null) != null) {
                if(gameController.backToMainMenu()) return "back";
                else return "Invalid command";
            }
            if (commandParser.validate(input, "show current menu", null) != null)
                System.out.println("Game menu");
            else if (commandParser.validate(input, "trade menu", null) != null) {
                System.out.println("You enter trade menu successfully!");
                return "trade";
            } else if (commandParser.validate(input, "shop menu", null) != null) {
                System.out.println("You enter shop menu successfully!");
                return "shop";
            } else if ((options = (commandParser.validate(input, "drop building", "x|xPosition/y|yPosition/t|type"))) != null)
                System.out.println(gameController.dropBuilding(options));
            else if ((options = commandParser.validate(input, "select building", "x|xPosition/y|yPosition")) != null) {
                result = gameController.selectBuilding(options);
                if (!result.equals("building")) System.out.println(result);
                else return "building";
            }  else if ((options = commandParser.validate(input, "select siege", "x|xPosition/y|yPosition")) != null) {
                result = gameController.selectSiege(options);
                if (!result.equals("building")) System.out.println(result);
                else return "building";
            } else if ((options = commandParser.validate(input, "select unit", "x|positionX/y|positionY/t|type")) != null) {
                result = gameController.selectUnit(options);
                if (!result.equals("unit")) System.out.println(result);
                else return "unit";
            }else if ((options = commandParser.validate(input,"show map","x|positionX/y|positionY")) != null) {
                result = gameController.showMap(options);
                System.out.println(result);
                if(result.matches("map:\\sin[\\S\\s]+")) return "map";
            }else if ((options = commandParser.validate(input, "tax rate", "r|rateNumber")) != null)
                System.out.println(gameController.setTaxRate(options));
            else if (commandParser.validate(input, "tax rate show", null) != null)
                System.out.println(gameController.showTaxRate());
            else if (commandParser.validate(input, "show popularity factors", null) != null)
                System.out.println(gameController.showPopularityFactors());
            else if (commandParser.validate(input, "show popularity", null) != null)
                System.out.println(gameController.showPopularity());
            else if (commandParser.validate(input, "show food list", null) != null)
                System.out.println(gameController.showFoodList());
            else if ((options = commandParser.validate(input, "food rate", "r|rateNumber")) != null)
                System.out.println(gameController.setFoodRate(options));
            else if (commandParser.validate(input, "food rate show", null) != null)
                System.out.println(gameController.showFoodRate());
            else if ((options = commandParser.validate(input, "fear rate", "r|rateNumber")) != null)
                System.out.println(gameController.setFearRate(options));
            else if ((options = commandParser.validate(input, "drop trap", "x|positionX/y|positionY/t|type")) != null)
                System.out.println(gameController.setFearRate(options));
            else if (commandParser.validate(input, "next turn", null) != null)
                return gameController.nextTurn();
            else System.out.println("invalid command");
        }
    }
}
