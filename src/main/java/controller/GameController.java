package controller;

import model.Map;
import model.ResourceType;
import model.Trade;
import model.User;
import view.*;

import java.util.HashMap;

public class GameController {
    private Map gameMap;
    private GameMenu gameMenu;
    private UnitMenu unitMenu;
    private BuildingMenu buildingMenu;
    private TradeMenu tradeMenu;
    private ShopMenu shopMenu;
    private User currentUser;

    public GameController(Map gameMap) {
        this.gameMap = gameMap;
        this.gameMenu = new GameMenu(this);
        this.shopMenu = new ShopMenu(this);
        this.unitMenu = new UnitMenu(this);
        this.buildingMenu = new BuildingMenu(this);
        this.tradeMenu = new TradeMenu(this);
        currentUser = Controller.currentUser;
    }

    public void run(){
        switch (gameMenu.run()){
            case "map":
                break;
        case "trade":
            showNotification();
            tradeMenu.run();
        case "shop":
            shopMenu.run();
            break;
        case "building":
            buildingMenu.run();
            break;
        case "unit":
            unitMenu.run();
            break;
        }
    }

    public String nextTurn(){
        return null;
    }

    public String newRequest(HashMap<String, String> options) {
        User userReceiver;
        for (String value : options.keySet()) {
            if (options.get(value) == null)
                return value + " option is not entered";
        }
        try {
            ResourceType resourceType = ResourceType.valueOf(options.get("t").toUpperCase());
            if ((userReceiver = User.getUserByUsername(options.get("u"))) != null) {
                int resourceAmount = Integer.parseInt(options.get("a"));
                int price = Integer.parseInt(options.get("p"));
                String massage = options.get("m");
                Trade trade = new Trade(resourceType, resourceAmount, price, currentUser, userReceiver, massage, Trade.countId);
                Trade.countId ++;
                Trade.getTrades().add(trade);
                currentUser.getMyRequests().add(trade);
                userReceiver.getMySuggestion().add(trade);
                userReceiver.getNotification().add(0, trade);
                return "The request was successfully registered";
            }
            else
                return "Username with this ID was not found";
        }
        catch (Exception IllegalArgumentException){
            return "This resource type does not exist";
        }
    }
    public String showTradeList() {
        String output = "your suggestions:";
        for (Trade trade : currentUser.getMySuggestion()) {
            output += "\nResource type: " + trade.getResourceType().name() + "resource amount: " + trade.getResourceAmount()
                    + "price: " + trade.getPrice() + "from: " + trade.getUserSender().getUserName()
                    + "id: " + trade.getId() + "massage: " + trade.getMassage();
        }
        currentUser.getNotification().clear();
        return output;
    }
    public String tradeAccept(HashMap<String, String> options) {
        for (String value : options.keySet()) {
            if (options.get(value) == null)
                return value + " option is not entered";
        }
        for (Trade trade : currentUser.getMySuggestion()) {
            if (trade.getId() == Integer.parseInt(options.get("i"))) {/////////////
                break;
            }
        }
        return "This ID was not found for you";
    }
    public String showTradeHistory() {
        String output = "your history:";
        for (Trade trade : currentUser.getHistoryTrade()) {
            output += "\nResource type: " + trade.getResourceType().name() + "resource amount: " + trade.getResourceAmount()
                    + "price: " + trade.getPrice()+ "id: " + trade.getId() + "massage: " + trade.getMassage();
            if (trade.getUserSender().equals(currentUser))
                output += " ...Requested...";
            else
                output += " ...Accepted...";
        }
        return output;
    }
    public String showNotification() {
        String output = "your new suggestions:";
        for (Trade trade : currentUser.getNotification()) {
            output += "\nnew suggestion form " + trade.getUserSender() + "massage: " + trade.getMassage();
        }
        return output;
    }

}
