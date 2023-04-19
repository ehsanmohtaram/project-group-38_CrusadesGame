package view;

import controller.Controller;
import controller.TradeController;
import model.User;

import java.util.regex.Matcher;

public class TradeMenu {
    private Controller controller;

    public TradeMenu (TradeController tradeController) {
        this.controller = controller;
    }
    public void run() {
        String allUsers = null;
        for (User user : User.users) {
            allUsers += user.getUserName();
        }
        System.out.print(allUsers);
        String input;
        Matcher matcher;
        //if and else

    }
}
