package view;

import controller.CommandParser;
import controller.Controller;

import java.util.HashMap;

public class ProfileMenu {
    private final Controller controller;
    private final CommandParser commandParser;
    public ProfileMenu (Controller controller) {
        this.controller = controller;
        commandParser = new CommandParser();
    }
    public String run() {
        HashMap<String, String> optionPass;
        String command;
        while (true) {
            command = CommandParser.getScanner().nextLine();
            if (commandParser.validate(command,"back",null) != null) return "back";
            if (commandParser.validate(command,"show current menu",null) != null) System.out.println("Profile Menu");
            else if ((optionPass = commandParser.validate(command,"profile change","u|username/n|nickname/e|email/s|slogan")) != null)
                System.out.println(controller.profileChange(optionPass));
            else if ((optionPass = commandParser.validate(command,"profile change password","o|oldPassword/n|newPassword")) != null)
                System.out.println(controller.changePassword(optionPass));
            else if (commandParser.validate(command,"profile remove slogan",null) != null)
                System.out.println(controller.removeSlogan());
            else if (commandParser.validate(command,"profile display highscore",null) != null)
                System.out.println(controller.displayInfoSeparately(true,false,false));
            else if (commandParser.validate(command,"profile display rank",null) != null)
                System.out.println(controller.displayInfoSeparately(false,true,false));
            else if (commandParser.validate(command,"profile display slogan",null) != null)
                System.out.println(controller.displayInfoSeparately(false,false,true));
            else if (commandParser.validate(command,"profile display",null) != null)
                System.out.println(controller.displayAllInfo());
            else System.out.println("Invalid command!");
        }
    }
}
