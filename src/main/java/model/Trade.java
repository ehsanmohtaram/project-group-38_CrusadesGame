package model;

import java.util.ArrayList;
import java.util.Objects;


public class Trade {
    private static ArrayList<Trade> trades = new ArrayList<>();
    private ResourceType resourceType;
    private Integer resourceAmount;
    private User userSender;
    private User userReceiver;
    private Integer price;
    private Integer id;
    private String massageRequest;
    private String massageAccept = null;
    public static Integer countId = 1;

    public Trade(ResourceType resourceType, Integer resourceAmount, Integer price, User userSender, User userReceiver, String massageRequest , Integer id) {
        this.resourceType = resourceType;
        this.resourceAmount = resourceAmount;
        this.userSender = userSender;
        this.userReceiver = userReceiver;
        this.price = price;
        this.massageRequest = massageRequest;
        this.id = id;
    }

    public static ArrayList<Trade> getTrades() {
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

    public String getMassageRequest() {
        return massageRequest;
    }

    public String getMassageAccept() {
        return massageAccept;
    }

    public void setMassageAccept(String massageAccept) {
        this.massageAccept = massageAccept;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Trade trade = (Trade) o;
        return resourceType == trade.resourceType && Objects.equals(resourceAmount, trade.resourceAmount) && Objects.equals(price, trade.price) && Objects.equals(id, trade.id) && Objects.equals(massageRequest, trade.massageRequest) && Objects.equals(massageAccept, trade.massageAccept);
    }

}
