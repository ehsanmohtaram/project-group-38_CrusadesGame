package controller;

import model.*;
import view.TradeMenu;

import java.util.HashMap;

public class TradeController {
    private final TradeMenu tradeMenu;
    private final Map gameMap;
    private final User currentUser;

    public TradeController() {
        this.tradeMenu = new TradeMenu(this);
        gameMap = GameController.gameMap;
        currentUser = Controller.currentUser;
    }

    public void runTrade() {
        StringBuilder output = new StringBuilder("All Users :");
        output.append(" / ");
        for (User user : User.users) output.append(user.getUserName()).append(" / ");
        System.out.println(output);
        System.out.println(showNotification());
        tradeMenu.run();
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
                gameMap.getKingdomByOwner(currentUser).addRequest(trade);
                gameMap.getKingdomByOwner(userReceiver).addSuggestion(trade);
                gameMap.getKingdomByOwner(userReceiver).addNotification(trade);
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
        for (Trade trade : gameMap.getKingdomByOwner(currentUser).getMySuggestion()) {
            output.append("\nResource type : ").append(trade.getResourceType().name())
                    .append(" resource amount : ").append(trade.getResourceAmount())
                    .append(" price : ").append(trade.getPrice()).append(" from : ")
                    .append(trade.getUserSender().getUserName()).append(" id : ").append(trade.getId())
                    .append(" massage : ").append(trade.getMassage());
        }
        gameMap.getKingdomByOwner(currentUser).getNotification().clear();
        return output.toString();
    }
    public String tradeAccept(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        for (Trade trade : gameMap.getKingdomByOwner(currentUser).getMySuggestion()) {
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
        for (Trade trade : gameMap.getKingdomByOwner(currentUser).getHistoryTrade()) {
            output.append("\nResource type : ").append(trade.getResourceType().name())
                    .append(" resource amount : ").append(trade.getResourceAmount())
                    .append(" price : ").append(trade.getPrice()).append("id: ")
                    .append(trade.getId()).append(" massage : ").append(trade.getMassage());
            if (trade.getUserSender().equals(currentUser)) output.append(" ...Requested... ");
            else output.append(" ...Accepted... ");
        }
        return output.toString();
    }
    public String showNotification() {
        StringBuilder output = new StringBuilder("your new suggestions : ");
        for (Trade trade : gameMap.getKingdomByOwner(currentUser).getNotification()) {
            output.append("\nnew suggestion from : ").append(trade.getUserSender().getUserName())
                    .append("massage : ").append(trade.getMassage());
        }
        return output.toString();
    }
}
