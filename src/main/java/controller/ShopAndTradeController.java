package controller;

import model.*;
import view.ShopMenu;
import view.TradeMenu;

import java.util.HashMap;

public class ShopAndTradeController {
    private final ShopMenu shopMenu;
    private final TradeMenu tradeMenu;
    private final Map gameMap;
    private final User currentUser;

    public ShopAndTradeController() {
        this.shopMenu = new ShopMenu(this);
        this.tradeMenu = new TradeMenu(this);
        gameMap = GameController.gameMap;
        currentUser = Controller.currentUser;
    }

    public void runTrade() {
        System.out.println(showNotification());
        tradeMenu.run();
    }

    public void runShop() {
        shopMenu.run();
    }

    public String newRequest(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        User userReceiver;
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
        StringBuilder output = new StringBuilder("your suggestions :");
        for (Trade trade : currentUser.getMySuggestion()) {
            output.append("\nResource type : ").append(trade.getResourceType().name())
                    .append("resource amount : ").append(trade.getResourceAmount())
                    .append("price : ").append(trade.getPrice()).append("from : ")
                    .append(trade.getUserSender().getUserName()).append("id : ").append(trade.getId())
                    .append("massage : ").append(trade.getMassage());
        }
        currentUser.getNotification().clear();
        return output.toString();
    }
    public String tradeAccept(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        for (Trade trade : currentUser.getMySuggestion()) {
            if (trade.getId() == Integer.parseInt(options.get("i"))) {
                Kingdom kingdom = gameMap.getKingdomByOwner(currentUser);
                int cost = trade.getPrice() * trade.getResourceAmount();
                if (kingdom.getBalance() > cost) {
                    kingdom.setBalance(kingdom.getBalance() - cost);
                    int resourceBalance = kingdom.getResources().get(trade.getResourceType());
                    kingdom.getResources().put(trade.getResourceType(), resourceBalance + trade.getResourceAmount());
                    return "The trade was successful";
                } else
                    return "Your balance is not enough";
            }
        }
        return "This ID was not found for you";
    }
    public String showTradeHistory() {
        StringBuilder output = new StringBuilder("your history:");
        for (Trade trade : currentUser.getHistoryTrade()) {
            output.append("\nResource type : ").append(trade.getResourceType().name())
                    .append("resource amount : ").append(trade.getResourceAmount())
                    .append("price : ").append(trade.getPrice()).append("id: ")
                    .append(trade.getId()).append("massage : ").append(trade.getMassage());
            if (trade.getUserSender().equals(currentUser)) output.append(" ...Requested... ");
            else output.append(" ...Accepted... ");
        }
        return output.toString();
    }
    public String showNotification() {
        StringBuilder output = new StringBuilder("your new suggestions :");
        for (Trade trade : currentUser.getNotification()) {
            output.append("\nnew suggestion from : ").append(trade.getUserSender())
                    .append("massage : ").append(trade.getMassage());
        }
        return output.toString();
    }
    public String showPriceList() {
        StringBuilder output = new StringBuilder("Resource Types :");
        for (ResourceType value : ResourceType.values()) {
            int balance = gameMap.getKingdomByOwner(currentUser).getResources().get(value);
            output.append("\nname : ").append(value.name().toLowerCase())
                    .append("buyPrice : ").append(value.getPrice())
                    .append("sellPrice : ").append(value.getPrice() * (0.8))
                    .append("balance : ").append(balance);
        }
        output.append("\nFoods :");
        for (Food value : Food.values()) {
            int balance = gameMap.getKingdomByOwner(currentUser).getFoods().get(value);
            output.append("\nname : ").append(value.name().toLowerCase())
                    .append("buyPrice : ").append(value.getPrice())
                    .append("sellPrice : ").append(value.getPrice() * (0.8))
                    .append("balance : ").append(balance);
        }
        return output.toString();
    }

    public String buyFromShop(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
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
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
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
