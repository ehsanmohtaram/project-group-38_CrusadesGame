package model;

import model.building.Building;
import model.building.BuildingType;
import model.building.Stock;
import model.unit.Unit;
import model.unit.UnitState;
import model.unit.UnitType;

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
    private Integer kingdomRange;
    private ArrayList<Trade> myRequests = new ArrayList<>();
    private ArrayList<Trade> mySuggestion = new ArrayList<>();
    private ArrayList<Trade> notification = new ArrayList<>();
    private ArrayList<Trade> historyTrade = new ArrayList<>();
    private HashMap<Food ,Integer> foods = new HashMap<>();
    private HashMap<ResourceType, Integer> resources = new HashMap<>();
    private HashMap<Weapons, Integer> weapons = new HashMap<>();
    private ArrayList<Unit> units = new ArrayList<>();
    private ArrayList<Building> buildings = new ArrayList<>();
    public Kingdom(Flags flag, User owner) {
        population = noneEmployed = 20;
        engineer = 0;
        popularity = 0;
        foodRate = -2;
        kingdomRange = 5;
        this.flag = flag;
        this.owner = owner;
        this.balance = 200.0;
        for (ResourceType resourceType : ResourceType.values()) resources.put(resourceType, 100);
        for (Food food : Food.values()) foods.put(food, 0);
        //TODO after dibug it should be put 0
        for (Weapons weapon : Weapons.values()) weapons.put(weapon, 100);
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

    public HashMap<Food, Integer> getFoods() {
        return foods;
    }

    public HashMap<ResourceType, Integer> getResources() {
        return resources;
    }

    public HashMap<Weapons, Integer> getWeapons() {
        return weapons;
    }

    public void setResourceAmount(ResourceType resourceType, Integer amount) {
        resources.put(resourceType,resources.get(resourceType) + amount);
        Stock stock = null;
        for (Building building : buildings)
            if (building.getBuildingType().equals(BuildingType.STOCKPILE)) {stock = (Stock) building; break;}
        stock.addResourceToStock(resourceType, resources.get(resourceType));
    }

    public void setFoodsAmount(Food food, Integer amount) {
        foods.put(food, foods.get(food) + amount);
        Stock stock = null;
        for (Building building : buildings)
            if (building.getBuildingType().equals(BuildingType.FOOD_STOCKPILE)) {stock = (Stock) building; break;}
        stock.addResourceToStock(food, foods.get(food));
    }

    public void setWeaponsAmount(Weapons weapon, Integer amount) {
        weapons.put(weapon, weapons.get(weapon) + amount);
        Stock stock = null;
        for (Building building : buildings)
            if (building.getBuildingType().equals(BuildingType.ARMOURY)) {stock = (Stock) building; break;}
        stock.addResourceToStock(weapon, weapons.get(weapon));
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

    public void setFoodAmount(Food toAdd , Integer amount){
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

    public Integer getWeaponAmount(Weapons weapon) {
        return weapons.get(weapon);
    }

    public Integer getFoodAmount(Food food) {
        return foods.get(food);
    }

    public Building getBuildingFormKingdom(BuildingType buildingType) {
        for (Building building : buildings) if (building.getBuildingType().equals(buildingType)) return building;
        return null;
    }

    public boolean checkOutOfRange(int xPosition , int yPosition) {
        int xResult = 0, yResult = 0, max = 0;
        for (Building building : buildings) {
            xResult = building.getPosition().getxPosition() - headquarter.getPosition().getxPosition();
            yResult = building.getPosition().getyPosition() - headquarter.getPosition().getyPosition();
            if (xResult * xResult + yResult * yResult > max) max = xResult * xResult + yResult * yResult;
        }
        xPosition = xPosition - headquarter.getPosition().getxPosition();
        yPosition = yPosition - headquarter.getPosition().getyPosition();
        return (xResult + 4) * (yResult + 4) + (xResult + 4) * (xResult + 4) >= xPosition * xPosition + yPosition * yPosition;
    }

    public int checkForAvailableNormalUnit(UnitType unitType) {
        int counter = 0;
        for (Unit unit : units)
            if (unit.getUnitType().equals(unitType) && unit.getUnitState().equals(UnitState.NOT_WORKING)) counter++;
        return counter;
    }

    public void setNormalUnitInPosition(UnitType unitType, MapBlock mapBlock, int numberOfWorker) {
        int counter = 0;
        for (Unit unit : units) {
            if (counter == numberOfWorker) return;
            if (unit.getUnitType().equals(unitType) && unit.getUnitState().equals(UnitState.NOT_WORKING)) {
                unit.setLocationBlock(mapBlock);
                unit.setUnitState(UnitState.WORKING);
                counter++;
            }
        }
    }
}
