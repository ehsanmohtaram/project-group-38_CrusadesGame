package view;

import controller.CommandParser;
import controller.Controller;
import model.User;

import java.util.HashMap;

public class TradeMenu {
    private Controller controller;
    private CommandParser commandParser = new CommandParser();
    public TradeMenu (Controller controller){
        this.controller = controller;
    }
    public void run() {
        String output = "all Users:";
        for (User user : User.users) {
            output += "\n" + user.getUserName();
        }
        System.out.println(output);
        HashMap<String, String> optionPass;
        String input;
        while (true) {
            input = CommandParser.getScanner().nextLine();
            if ((optionPass = commandParser.validate(input, "trade", "t|resourceType/a|resourceAmount/p|price/m|massage/u|username")) != null)
                System.out.println(controller.newRequest(optionPass));
            else if (input.matches("\\s*trade\\s+list\\s*"))
                System.out.println(controller.showTradeList());
            else if ((optionPass = commandParser.validate(input, "trade accept","i|id/m|massage")) != null)
                System.out.println(controller.tradeAccept(optionPass));
            else if (input.matches("\\s*trade\\s+history\\s*"))
                System.out.println(controller.showTradeHistory());
            else
                System.out.println("Invalid Command");
        }

    }
}
