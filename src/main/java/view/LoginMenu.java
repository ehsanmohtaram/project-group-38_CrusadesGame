package view;

import controller.CommandParser;
import controller.Controller;
import controller.LoginController;

import java.util.HashMap;

public class LoginMenu {
    private Controller controller;
    private CommandParser commandParser;
    public LoginMenu (Controller controller) {
        this.controller = controller;
        commandParser = new CommandParser();
    }
    public String run() {
        HashMap<String, String> optionPass = new HashMap<>();
        String command;
        while (true) {
            command = CommandParser.getScanner().nextLine();
            if (command.matches("^exit$")) return "exit";
            if ((optionPass = commandParser.validate(command,"user create","u|username/p|password/n|nickname/e|email")) != null)
                System.out.println(Controller.createUser(optionPass));
        }
    }
}
