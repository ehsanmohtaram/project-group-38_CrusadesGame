package view;

import controller.CommandParser;
import controller.Controller;
import controller.GameController;

import java.util.HashMap;

public class ShopMenu {
    private GameController gameController;
    private CommandParser commandParser;
    public ShopMenu (GameController controller) {
        this.gameController = controller;

    }
    public String run () {
        HashMap<String, String> optionPass;
        String input;
        while (true) {
            input = CommandParser.getScanner().nextLine();
            if ((optionPass = commandParser.validate(input, "show price list", null)) != null)
                System.out.println(gameController.showPriceList());
            else if ((optionPass = commandParser.validate(input, "buy" , "i|itemName/a|itemAmount")) != null)
                System.out.println(gameController.buyFromShop(optionPass));
            else if ((optionPass = commandParser.validate(input, "sell" , "i|itemName/a|itemAmount")) != null)
                System.out.println(gameController.sellFromShop(optionPass));
            else
                System.out.println("Invalid Command");
        }
    }
}
