package model;

import model.building.*;
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
    private HashMap<Food ,Integer> foods = new HashMap<>();
    private HashMap<ResourceType, Integer> resources = new HashMap<>();
    private HashMap<Weapons, Integer> weapons = new HashMap<>();
    private ArrayList<Unit> units = new ArrayList<>();
    private ArrayList<Building> buildings = new ArrayList<>();
    private ArrayList<Unit> remainingUnitMove = new ArrayList<>();
    private ArrayList<Building> remainingBuildingMove = new ArrayList<>();
    private HashMap<Building, Integer> moveOfCows = new HashMap<>();
    private ArrayList<Trap> traps = new ArrayList<>();
    public Kingdom(Flags flag, User owner) {
        population = noneEmployed = 10;
        popularity = 10;
        foodRate = -2;
        fearRate = 0;
        taxRate = 0;
        this.flag = flag;
        this.owner = owner;
        this.balance = 10000.0;
        for (ResourceType resourceType : ResourceType.values()) resources.put(resourceType, 50);
        for (Weapons weapon : Weapons.values()) weapons.put(weapon, 0);
        for (Food food : Food.values()) foods.put(food, 0);
    }

    public User getOwner() {
        return owner;
    }

    public void setHeadquarter(Building headquarter) {
        this.headquarter = headquarter;
    }

    public Building getHeadquarter() {
        return headquarter;
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

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void addTrap(Trap trap){
        traps.add(trap);
    }

    public ArrayList<Trap> getTraps() {
        return traps;
    }

    public void setPopulation(Integer population) {
        this.population += population;
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

    public void setPopularity(Integer popularity) {
        this.popularity += popularity;
    }

    public Integer getPopularity() {
        return popularity;
    }

    public Integer getTaxRate() {
        return taxRate;
    }

    public Integer getMoveOfCows(Building mine) {
        return moveOfCows.get(mine);
    }

    public void setMoveOfCows(Building mine, Integer moves) {
        moveOfCows.put(mine, moves);
    }

    public Camp getCampsToDisbandUnit(ArrayList<Unit> toDisband){
        for (Building building : buildings) {
            UnitType unitType = toDisband.get(0).getUnitType();
            if(building instanceof Camp){
                Camp camp = (Camp) building;
                CampType campType = (CampType) building.getSpecificConstant();
                if(campType.getCapacity() >= (camp.getCapacity() + toDisband.size())) {
                    if (unitType.equals(UnitType.ENGINEER) && campType.equals(CampType.ENGINEER_GUILD))
                        return camp;
                    if (unitType.getIS_ARAB().equals(1) && campType.equals(CampType.MERCENARY_POST))
                        return camp;
                    if(unitType.getIS_ARAB().equals(0) && campType.equals(CampType.BARRACK))
                        return camp;
                    if(unitType.getIS_ARAB().equals(-2) && campType.equals(CampType.CHURCH) ||
                            unitType.getIS_ARAB().equals(-3) && campType.equals(CampType.CATHEDRAL))
                        return camp;
                }
            }
        }
        return null;
    }

    public Integer getAttackRate() {
        double fearRateSet = (double) fearRate / 10;
        return (int) fearRateSet + 1;
    }

    public void setTaxRate(Integer taxRate) {
        this.taxRate = taxRate;
    }

    public HashMap<Food, Integer> getFoods() {
        return foods;
    }

    public Integer getNumberOfStock(BuildingType stockType) {
        int counter = 0;
        for (Building building : buildings) if (building.getBuildingType().equals(stockType)) counter++;
        return counter;
    }

    public ArrayList<Unit> getRemainingUnitMove() {
        return remainingUnitMove;
    }

    public ArrayList<Building> getRemainingBuildingMove() {
        return remainingBuildingMove;
    }

    public void setRemainingUnitMove(Unit unit) {
        remainingUnitMove.add(unit);
    }

    public void setRemainingBuildingMove(Building building) {
        remainingBuildingMove.add(building);
    }

    public void setStockCapacity(StockType stockType, int currentResourceAmount, Enum<?> resources) {
        Stock stock;
        int fullStock = currentResourceAmount / stockType.getCAPACITY();
        int notFullStock = currentResourceAmount % stockType.getCAPACITY();
        int fullCheck = 0;
        for (Building building : buildings) {
            if (fullCheck == fullStock) break;
            if (building.getBuildingType().equals(BuildingType.valueOf(stockType.name()))) {
                stock = (Stock) building;
                stock.addResourceToStock(resources, stockType.getCAPACITY());
                fullCheck++;
            }
        }
        for (Building building : buildings) {
            if (building.getBuildingType().equals(BuildingType.valueOf(stockType.name()))) {
                stock = (Stock) building;
                if (!stock.getAmountByName(resources).equals(stockType.getCAPACITY())) {
                    stock.addResourceToStock(resources, notFullStock);break;
                }
            }
        }
    }

    public void setResourceAmount(ResourceType resourceType, Integer amount) {
        resources.put(resourceType,resources.get(resourceType) + amount);
        setStockCapacity(StockType.STOCKPILE, resources.get(resourceType), resourceType);
    }

    public void setFoodsAmount(Food food, Integer amount) {
        foods.put(food, foods.get(food) + amount);
        setStockCapacity(StockType.FOOD_STOCKPILE, foods.get(food), food);
    }

    public void setWeaponsAmount(Weapons weapon, Integer amount) {
        weapons.put(weapon, weapons.get(weapon) + amount);
        setStockCapacity(StockType.ARMOURY, weapons.get(weapon), weapon);
    }

    public Integer getFoodRate() {
        return foodRate;
    }

    public Flags getFlag() {
        return flag;
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

    public void ChangePopularity(Integer amount) {
        this.popularity += amount;
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

    public Integer getMaxPopulation() {
        int counter = 10;
        for (Building building : buildings) if (building.getBuildingType().equals(BuildingType.HOUSE)) counter += 8;
        return counter;
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

    public void changeNonWorkingUnitPosition(UnitType unitType, MapBlock mapBlock, int number) {
        Camp camp;
        int counter = 0;
        for (Unit unit : units) {
            if (counter == number) return;
            if (unit.getUnitType().equals(unitType) && unit.getUnitState().equals(UnitState.NOT_WORKING)) {
                if (unit.getLocationBlock().getBuildings() instanceof Camp) {
                    camp = (Camp) unit.getLocationBlock().getBuildings();
                    camp.getUnits().remove(unit);
                    camp.setCapacity(-1);
                }
                mapBlock.setUnits(unit);
                unit.getLocationBlock().getUnits().remove(unit);
                unit.setLocationBlock(mapBlock);
                unit.setUnitState(UnitState.WORKING);
                counter++;
            }
        }
    }

}
