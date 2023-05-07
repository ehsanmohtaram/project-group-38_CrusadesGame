package view;

import controller.CommandParser;
import controller.ShopAndTradeController;

import java.util.HashMap;

public class TradeMenu {
    private final ShopAndTradeController shopAndTradeController;
    private final CommandParser commandParser;
    public TradeMenu (ShopAndTradeController shopAndTradeController){
        this.shopAndTradeController = shopAndTradeController;
        commandParser = new CommandParser();
    }
    public String run() {
        HashMap<String, String> optionPass;
        String input;
        while (true) {
            input = CommandParser.getScanner().nextLine();
            if (commandParser.validate(input, "back", null) != null) return "back";
            if (commandParser.validate(input, "show current menu", null) != null)
                System.out.println("Trade menu");
            else if ((optionPass = commandParser.validate(input, "trade", "t|resourceType/a|resourceAmount/p|price/m|massage/u|username")) != null)
                System.out.println(shopAndTradeController.newRequest(optionPass));
            else if (commandParser.validate(input,"trade list",null) != null)
                System.out.println(shopAndTradeController.showTradeList());
            else if ((optionPass = commandParser.validate(input, "trade accept","i|id/m|massage")) != null)
                System.out.println(shopAndTradeController.tradeAccept(optionPass));
            else if (commandParser.validate(input,"trade history",null) != null)
                System.out.println(shopAndTradeController.showTradeHistory());
            else System.out.println("Invalid Command");
        }
    }
}
