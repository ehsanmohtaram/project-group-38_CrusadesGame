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
        System.out.println("dl");
//        String allUsers = null;
//        for (User user : User.users) {
//            allUsers += user.getUserName();
//        }
//        System.out.print(allUsers);
        HashMap<String, String> optionPass = new HashMap<>();
        String input;
        while (true) {
            input = CommandParser.getScanner().nextLine();
            if ((optionPass = commandParser.validate(input, "trade", "t|resourceType/a|resourceAmount/p|price/m|massage/u|username")) != null) {
                System.out.println(controller.newRequest(optionPass));
            }
            else
                System.out.println("Invalid Command");
        }

    }
}
