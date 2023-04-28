package model;

import model.building.Building;
import model.unit.Unit;

import java.util.ArrayList;
import java.util.HashMap;

public class Kingdom {
    private final User owner;
    private final Flags flag;
    private Integer population;
    private Integer noneEmployed;
    private Integer fearRate;
    private Integer popularity;
    private Double balance;
    private Integer foodRate;
    private Integer taxRate;
    private Building headquarter;
    private ArrayList<Trade> myRequests = new ArrayList<>();

    private ArrayList<Trade> mySuggestion = new ArrayList<>();

    private ArrayList<Trade> notification = new ArrayList<>();

    private ArrayList<Trade> historyTrade = new ArrayList<>();
    private ArrayList<Building> cart  = new ArrayList<>();
    private HashMap<Food ,Integer> foods = new HashMap<>();
    private HashMap<ResourceType, Integer> resources = new HashMap<>();
    private ArrayList<Unit> units = new ArrayList<>();
    private ArrayList<Building> buildings = new ArrayList<>();
    public Kingdom(Flags flag, User owner, Building headquarter) {
        population = 5;
        popularity = 0;
        foodRate = -2;
        this.flag = flag;
        this.owner = owner;
        this.balance = 200.0;
        this.headquarter = headquarter;
        for (ResourceType resourceType : ResourceType.values()) resources.put(resourceType, 100);
        addBuilding(headquarter);
    }

    public User getOwner() {
        return owner;
    }

    public Integer getPopulation() {
        return population;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Integer getNoneEmployed() {
        return noneEmployed;
    }

    public void setNoneEmployed(Integer noneEmployed) {
        this.noneEmployed = noneEmployed;
    }

    public Integer getFearRate() {
        return fearRate;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public Integer getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(Integer taxRate) {
        this.taxRate = taxRate;
    }

    public HashMap<Food, Integer> getFoods() {
        return foods;
    }

    public HashMap<ResourceType, Integer> getResources() {
        return resources;
    }

    public Integer getFoodRate() {
        return foodRate;
    }

    public Flags getFlag() {
        return flag;
    }

    public Building getHeadquarter() {
        return headquarter;
    }

    public ArrayList<Trade> getMyRequests() {
        return myRequests;
    }

    public void addRequest(Trade trade) {
        myRequests.add(trade);
    }

    public void addSuggestion(Trade trade) {
        mySuggestion.add(trade);
    }

    public void addNotification(Trade trade) {
        notification.add(trade);
    }
    public ArrayList<Trade> getHistoryTrade() {
        return historyTrade;
    }

    public ArrayList<Trade> getMySuggestion() {
        return mySuggestion;
    }

    public ArrayList<Trade> getNotification() {
        return notification;
    }

    public void setFoodRate(Integer foodRate) {
        this.foodRate = foodRate;
    }

    public void ChangeFearRate(Integer amount) {
        this.fearRate += amount;
    }

    public void ChangePopularity(Integer amount) {
        this.popularity += amount;
    }

    public void addFood(Food toAdd , int amount){
        foods.put(toAdd , amount);
    }

    public void addUnit(Unit toAdd){
        units.add(toAdd);
    }

    public void addBuilding(Building toAdd){
        buildings.add(toAdd);
    }

    public Integer getResourceAmount(ResourceType resourceType) {
        return resources.get(resourceType);
    }




}
