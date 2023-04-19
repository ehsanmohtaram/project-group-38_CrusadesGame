package model;

import java.util.ArrayList;

public class Trade {
    private static ArrayList<Trade> trades = new ArrayList<>();
    private ResourceType resourceType;
    private Integer resourceAmount;
    private User userSender;
    private User userReceiver;
    private Integer price;
    private Integer id;
    private String massage;

    public Trade(ResourceType resourceType, Integer resourceAmount, Integer price, User userSender, User userReceiver, String massage) {
        this.resourceType = resourceType;
        this.resourceAmount = resourceAmount;
        this.userSender = userSender;
        this.userReceiver = userReceiver;
        this.price = price;
        this.massage = massage;
    }

    public ArrayList<Trade> getTrades() {
        return trades;
    }

    public ResourceType getResourceType() {
        return resourceType;
    }

    public Integer getResourceAmount() {
        return resourceAmount;
    }

    public User getUserSender() {
        return userSender;
    }

    public User getUserReceiver() {
        return userReceiver;
    }

    public Integer getPrice() {
        return price;
    }

    public Integer getId() {
        return id;
    }

    public String getMassage() {
        return massage;
    }

    public static void newTrade(Trade trade) {

    }
}
