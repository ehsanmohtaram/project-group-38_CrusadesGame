package view;

import controller.CommandParser;
import controller.ShopAndTradeController;

import java.util.HashMap;

public class ShopMenu {

    private final ShopAndTradeController shopAndTradeController;
    private final CommandParser commandParser;
    public ShopMenu (ShopAndTradeController shopAndTradeController) {
        commandParser = new CommandParser();
        this.shopAndTradeController = shopAndTradeController;

    }
    public void run () {
        HashMap<String, String> optionPass;
        String input;
        while (true) {
            input = CommandParser.getScanner().nextLine();
            if (commandParser.validate(input, "back", null) != null) return;
            if (commandParser.validate(input, "show current menu", null) != null)
                System.out.println("Shop menu");
            if (commandParser.validate(input, "show price list", null) != null)
                System.out.println(shopAndTradeController.showPriceList());
            else if ((optionPass = commandParser.validate(input, "buy" , "i|itemName/a|itemAmount")) != null)
                System.out.println(shopAndTradeController.buyFromShop(optionPass));
            else if ((optionPass = commandParser.validate(input, "sell" , "i|itemName/a|itemAmount")) != null)
                System.out.println(shopAndTradeController.sellFromShop(optionPass));
            else System.out.println("Invalid Command");
        }
    }
}
