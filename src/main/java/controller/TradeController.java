package controller;

import model.ResourceType;
import model.Trade;
import model.User;
import view.TradeMenu;

import java.util.regex.Matcher;

public class TradeController {
    private TradeMenu tradeMenu;
    public static User currentUser;

    public TradeController() {
        this.tradeMenu = new TradeMenu(this);
    }

    public void newRequest(Matcher matcher){
        int resourceAmount = Integer.parseInt(matcher.group("resourceAmount"));
        int price = Integer.parseInt(matcher.group("price"));
        String massage = matcher.group("matcher");
        User userReceiver = User.getUserByUsername(matcher.group("user"));
        try {
            ResourceType resourceType = ResourceType.valueOf(matcher.group("resourceType").toUpperCase());
            if (matcher.group("negativeAmount") != null) {
                if (matcher.group("negativePrice") != null) {
                    Trade trade = new Trade(resourceType, resourceAmount, price, currentUser, userReceiver, massage);
                    Trade.newTrade(trade);
                }
                else
                    System.out.println("Invalid price");
            }
            else
                System.out.println("Invalid resource Amount");

        } catch (Exception IllegalArgumentException) {
            System.out.println("Invalid resource Type");
        }
    }
}
