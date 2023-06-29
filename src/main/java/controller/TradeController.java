package controller;

import model.*;
import model.building.BuildingType;
import model.building.StockType;
import java.util.HashMap;

public class TradeController {
    private final Map gameMap;
    private final User currentUser;

    public TradeController(Map gameMap) {
        this.gameMap = gameMap;
        currentUser = Controller.currentUser;
    }

    public String newRequest(HashMap<String, String> options) {
        ResourceType resourceType = ResourceType.valueOf(options.get("t"));
        User userReceiver = User.getUserByUsername(options.get("u"));
        int resourceAmount = Integer.parseInt(options.get("a"));
        int price = Integer.parseInt(options.get("p"));
        String massageRequest = options.get("m");
        if (gameMap.getKingdomByOwner(currentUser).getNumberOfStock(BuildingType.STOCKPILE) * StockType.STOCKPILE.getCAPACITY() <
                gameMap.getKingdomByOwner(currentUser).getResourceAmount(resourceType) + resourceAmount)
            return "You do not have enough space!";
        if (gameMap.getKingdomByOwner(currentUser).getBalance() <= price * resourceAmount) return "your balance not enough";
        Trade trade = new Trade(resourceType, resourceAmount, price, currentUser, userReceiver, massageRequest, Trade.countId);
        Trade.countId++;
        Trade.getTrades().add(trade);
        gameMap.getKingdomByOwner(currentUser).addRequest(trade);
        gameMap.getKingdomByOwner(currentUser).getHistoryTrade().add(trade);
        gameMap.getKingdomByOwner(userReceiver).addSuggestion(trade);
        gameMap.getKingdomByOwner(userReceiver).addNotification(trade);
        return "done";
    }

    public String tradeAccept(HashMap<String, String> options) {
        int id = Integer.parseInt(options.get("i"));
        Trade trade = null;
        for (Trade check : gameMap.getKingdomByOwner(currentUser).getMySuggestion())
            if (check.getId() == id){
                trade = check; break;
            }
        Kingdom receiverkingdom = gameMap.getKingdomByOwner(currentUser);
        if (receiverkingdom.getResourceAmount(trade.getResourceType()) <= trade.getResourceAmount())
            return "The resource is not enough!";
        receiverkingdom.setResourceAmount(trade.getResourceType(), -1 * trade.getResourceAmount());
        receiverkingdom.setBalance((double) trade.getPrice() * trade.getResourceAmount());
        receiverkingdom.getHistoryTrade().add(trade);
        receiverkingdom.getMySuggestion().remove(trade);
        Kingdom senderKingdom = gameMap.getKingdomByOwner(trade.getUserSender());
        senderKingdom.setResourceAmount(trade.getResourceType(), trade.getResourceAmount());
        senderKingdom.setBalance((double) -trade.getPrice() * trade.getResourceAmount());
        trade.setMassageAccept(options.get("m"));
        senderKingdom.getNotification().add(trade);
        return "The trade was successful";
    }

    public String showNotification() {
        StringBuilder output = new StringBuilder("your new notification :");
        for (Trade trade : gameMap.getKingdomByOwner(currentUser).getNotification()) {
            if (trade.getUserSender().equals(currentUser)) {
                output.append("\nyour request accepted by--> ").append(trade.getUserReceiver().getUserName()).
                        append("\nmassage: ").append(trade.getMassageAccept());
            } else {
                output.append("\nnew suggestion by--> ").append(trade.getUserSender().getUserName()).
                        append("\nmassage: ").append(trade.getMassageRequest());
            }
        }
        gameMap.getKingdomByOwner(currentUser).getNotification().clear();
        return output.toString();
    }
}
