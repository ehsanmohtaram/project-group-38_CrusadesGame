package controller;

import model.*;
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
    public String showPriceList() {
        String output = "Resource Types:";
        for (ResourceType value : ResourceType.values()) {
            int balance = gameMap.getKingdomByOwner(currentUser).getResources().get(value);
            output += "\nname: " + value.name().toLowerCase() + " buyPrice: " + value.getPrice() +
                    " sellPrice: " + value.getPrice()*(0.8) + " balance: " + balance;
        }
        output += "\nfoods:";
        for (Food value : Food.values()) {
            int balance = gameMap.getKingdomByOwner(currentUser).getFoods().get(value);
            output += "\nname: " + value.name().toLowerCase() + " buyPrice: " + value.getPrice() +
                    " sellPrice: " + value.getPrice()*(0.8) + " balance: " + balance;
        }
        return output;
    }

    public String buyFromShop(HashMap<String, String> options) {
        Kingdom kingdom = gameMap.getKingdomByOwner(currentUser);
        int amount = Integer.parseInt(options.get("a"));
        try {
            ResourceType resourceType = ResourceType.valueOf(options.get("i").toUpperCase());
            if (amount * resourceType.getPrice() < kingdom.getBalance()) {
                int resourceBalance = kingdom.getResources().get(resourceType);
                kingdom.getResources().put(resourceType, resourceBalance + amount);
                double cost = amount * resourceType.getPrice();
                kingdom.setBalance(kingdom.getBalance() - cost);
                return "The purchase was successful";
            } else
                return "Your balance is not enough";
        }  catch (IllegalArgumentException illegalArgumentException){
            try {
                Food food = Food.valueOf(options.get("i").toUpperCase());
                if (amount * food.getPrice() < kingdom.getBalance()) {
                    int balance = kingdom.getFoods().get(food);
                    kingdom.getFoods().put(food, balance + amount);
                    int cost = amount * food.getPrice();
                    kingdom.setBalance(kingdom.getBalance() - cost);
                    return "The purchase was successful";
                } else
                    return "Your balance is not enough";
            } catch (IllegalArgumentException illegalArgumentException1){
                return "There are no imported resources or food";
            }

        }
    }

    public String sellFromShop(HashMap<String, String> options) {
        Kingdom kingdom = gameMap.getKingdomByOwner(currentUser);
        int amount = Integer.parseInt(options.get("a"));
        try {
            ResourceType resourceType = ResourceType.valueOf(options.get("i").toUpperCase());
            int resourceBalance = kingdom.getResources().get(resourceType);
            if (resourceBalance >= amount) {
                double benefit = amount * resourceType.getPrice() * 0.8;
                kingdom.setBalance(kingdom.getBalance() + benefit);
                kingdom.getResources().put(resourceType, resourceBalance - amount);
                return "The sell was successful";
            } else
                return "Your balance is not enough";

        } catch (IllegalArgumentException illegalArgumentException) {
            try {
                Food food = Food.valueOf(options.get("i").toUpperCase());
                int foodBalance = kingdom.getFoods().get(food);
                if (foodBalance >= amount) {
                    double benefit = amount * food.getPrice() * 0.8;
                    kingdom.setBalance(kingdom.getBalance() + benefit);
                    kingdom.getFoods().put(food, foodBalance - amount);
                    return "The sell was successful";
                } else
                    return "Your balance is not enough";
            } catch (IllegalArgumentException illegalArgumentException1) {
                return "There are no imported resources or food";
            }
        }
    }

}
