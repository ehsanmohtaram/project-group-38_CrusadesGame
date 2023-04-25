package view;

import controller.CommandParser;
import controller.LoginController;

import java.util.HashMap;

public class LoginMenu {
    private final LoginController loginController;
    private final CommandParser commandParser;
    public LoginMenu (LoginController loginController) {
        this.loginController = loginController;
        commandParser = new CommandParser();
    }
    public String run() {
        HashMap<String, String> optionPass;
        String command ,result;
        while (true) {
            command = CommandParser.getScanner().nextLine();
            if (commandParser.validate(command,"exit",null) != null) return "exit";
            if (commandParser.validate(command,"show current menu",null) != null) System.out.println("Login Menu");
            else if ((optionPass = commandParser.validate(command,"user create","u|username/?p|password/n|nickname/e|email/s|slogan")) != null) {
                result = loginController.createUser(optionPass);
                if (result != null) System.out.println(result);
            }
            else if ((optionPass = commandParser.validate(command,"user login","u|username/p|password/s|stay-logged-in")) != null) {
                result = loginController.login(optionPass);
                if (result.equals("login")) {System.out.println("User logged in successfully!"); return "login";}
                else System.out.println(result);
            }
            else if ((optionPass = commandParser.validate(command,"forgot my password","u|username")) != null)
                System.out.println(loginController.forgetPassword(optionPass));
            else System.out.println("Invalid command!");
        }
    }
}
