package model;

import model.building.Building;
import model.unit.Unit;

import java.util.ArrayList;
import java.util.HashMap;

public class Kingdom{
    private final User owner;
    private final Flags flag;
    private Integer population;
    private Integer noneEmployed;
    private Integer engineer;
    private Integer fearRate;
    private Integer maxFearRate;
    private Integer popularity;
    private Double balance;
    private Integer foodRate;
    private Integer taxRate;
    private Integer efficiency;
    private Building headquarter;
    private ArrayList<Trade> myRequests = new ArrayList<>();
    private ArrayList<Trade> mySuggestion = new ArrayList<>();
    private ArrayList<Trade> notification = new ArrayList<>();
    private ArrayList<Trade> historyTrade = new ArrayList<>();
    private HashMap<Food ,Double> foods = new HashMap<>();
    private HashMap<ResourceType, Integer> resources = new HashMap<>();
    private ArrayList<Unit> units = new ArrayList<>();
    private ArrayList<Building> buildings = new ArrayList<>();
    public Kingdom(Flags flag, User owner) {
        population = noneEmployed = 5;
        engineer = 0;
        popularity = 0;
        foodRate = -2;
        this.flag = flag;
        this.owner = owner;
        this.balance = 200.0;
        for (ResourceType resourceType : ResourceType.values())
            resources.put(resourceType, 100);
        for (Food food : Food.values())
            foods.put(food, 0.0);
    }

    public User getOwner() {
        return owner;
    }

    public void setHeadquarter(Building headquarter) {
        this.headquarter = headquarter;
    }

    public Integer getPopulation() {
        return population;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance += balance;
    }

    public void setFearRate(Integer fearRate) {
        this.fearRate = fearRate;
    }

    public void setEfficiency(Integer efficiency) {
        this.efficiency = efficiency;
    }

    public Integer getMaxFearRate() {
        return maxFearRate;
    }

    public void setMaxFearRate(Integer maxFearRate) {
        this.maxFearRate = maxFearRate;
    }

    public Integer getEfficiency() {
        return efficiency;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setPopulation(Integer population) {
        this.population = population;
    }

    public Integer getNoneEmployed() {
        return noneEmployed;
    }

    public void setNoneEmployed(Integer noneEmployed) {
        this.noneEmployed += noneEmployed;
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

    public HashMap<Food, Double> getFoods() {
        return foods;
    }

    public HashMap<ResourceType, Integer> getResources() {
        return resources;
    }
    public void setResourceAmount(ResourceType resourceType, Integer amount) {
        resources.put(resourceType,resources.get(resourceType) + amount);
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

    public ArrayList<Building> getBuildings() {
        return buildings;
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

    public Integer getEngineer() {
        return engineer;
    }

    public void setPopularity(Integer popularity) {
        this.popularity = popularity;
    }

    public void setEngineer(Integer engineer) {
        this.engineer = engineer;
    }

    public void ChangeFearRate(Integer amount) {
        this.fearRate += amount;
    }

    public void ChangePopularity(Integer amount) {
        this.popularity += amount;
    }

    public void addFood(Food toAdd , double amount){
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
