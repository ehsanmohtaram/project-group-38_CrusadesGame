package controller;
import model.*;
import model.building.*;
import model.unit.Unit;
import model.unit.UnitType;

public class Turn {

    private final Map gameMap;
    private final Kingdom currentKingdom;
    private final Kingdom lastKingdom;

    public Turn() {
        gameMap = GameController.gameMap;
        User currentUser = Controller.currentUser;
        currentKingdom = gameMap.getKingdomByOwner(currentUser);
        int result = ((gameMap.getPlayers().indexOf(currentKingdom) - 1) + gameMap.getPlayers().size()) % gameMap.getPlayers().size();
        lastKingdom = gameMap.getPlayers().get(result);
    }


    public void runNextTurn() {
        if (currentKingdom.getPopularity() > -5) {
            executeProducerBuilding();
            executeMines();
            growPopulation();
            innCheck();
            produceHorse();
        }
        setReligiousBuildingPopularity();
        giveFood();
        getTax();
        move();
    }

    public void growPopulation() {
        int foodCounter = 0;
        for (Food food : currentKingdom.getFoods().keySet()) foodCounter += currentKingdom.getFoods().get(food);
        int addPeople = currentKingdom.getMaxPopulation() - currentKingdom.getPopulation();
        int growthAmount = addPeople - (int)((((double) addPeople + (double) currentKingdom.getPopulation()) / (double) foodCounter) * (double) addPeople);
        if (addPeople >= 0 && growthAmount >= 0) currentKingdom.setPopulation(growthAmount);
    }

    public void getTax() {
        double getTax;
        if (currentKingdom.getTaxRate() <= 0) {
            getTax = currentKingdom.getTaxRate() * 0.2 - 0.4;
            if (currentKingdom.getTaxRate() == 0) getTax = 0;
            currentKingdom.setPopularity(-currentKingdom.getTaxRate() * 2 + 1);
        }
        else {
            getTax = currentKingdom.getTaxRate() * 0.2 + 0.4;
            currentKingdom.setPopularity(-currentKingdom.getTaxRate() * 2);
        }
        currentKingdom.setBalance(getTax * currentKingdom.getPopulation() * (gameMap.getPlayers().size() - 1));
    }

    public void giveFood() {
        if (currentKingdom.getFoodRate().equals(-2)){currentKingdom.setPopularity((gameMap.getPlayers().size() - 1) * -8); return;}
        int foodCounter = 0, setFood, checkFoodAmount = 0, variety = 0;
        int getFood = (int) (((double)(currentKingdom.getFoodRate() + 2) / 2) * (double)currentKingdom.getPopulation());
        for (Food food : currentKingdom.getFoods().keySet()) foodCounter += currentKingdom.getFoods().get(food);
        for (Food food : currentKingdom.getFoods().keySet()) {
            if (currentKingdom.getFoods().get(food) > 0) variety++;
            setFood = (int)(((double) currentKingdom.getFoods().get(food) / (double) foodCounter) * (double) getFood);
            checkFoodAmount += setFood;
            currentKingdom.setFoodsAmount(food, -setFood);
        }
        if (variety != 0) currentKingdom.setPopularity((gameMap.getPlayers().size() - 1) * 2);
        currentKingdom.setPopularity((gameMap.getPlayers().size() - 1) * currentKingdom.getFoodRate() * 4);
        checkFoodAmount = getFood - checkFoodAmount;
        if (checkFoodAmount == 0) return;
        while (true) {
            for (Food food : currentKingdom.getFoods().keySet()) {
                if (currentKingdom.getFoods().get(food) > 0) {
                    currentKingdom.setFoodsAmount(food, -1); checkFoodAmount--;
                    if (checkFoodAmount == 0) return;
                }
            }
        }

    }

    public void innCheck() {
        for (Building building : currentKingdom.getBuildings())
            if (building.getBuildingType().equals(BuildingType.INN)) {
                currentKingdom.setPopularity(2 * (gameMap.getPlayers().size() - 1));
                if (currentKingdom.getResourceAmount(ResourceType.HOP) - (gameMap.getPlayers().size() - 1) * 5 < 0)
                    currentKingdom.setResourceAmount(ResourceType.HOP.getBaseSource(), -currentKingdom.getResourceAmount(ResourceType.HOP));
                else
                    currentKingdom.setResourceAmount(ResourceType.HOP.getBaseSource(), -(gameMap.getPlayers().size() - 1) * 5);
            }
    }

    public void setReligiousBuildingPopularity() {
        currentKingdom.setPopularity(-currentKingdom.getFearRate() * (gameMap.getPlayers().size() - 1));
        int popularity = 0;
        for (Building building : currentKingdom.getBuildings())
            if (building.getBuildingType().equals(BuildingType.CATHEDRAL) || building.getBuildingType().equals(BuildingType.CHURCH))
                popularity += 2;
        popularity += popularity * (gameMap.getPlayers().size() -1);
        currentKingdom.setPopularity(popularity);
    }

    public void produceHorse() {
        Camp camp;
        CampType campType;
        for (Building building : currentKingdom.getBuildings())
            if (building.getSpecificConstant().equals(CampType.STABLE)) {
                campType = (CampType) building.getSpecificConstant();
                camp = (Camp) building;
                if (camp.getCapacity().equals(campType.getCapacity())) continue;
                camp.setCapacity(1);
                currentKingdom.setBalance((double) -20);
            }
    }


    public Integer setProduceRate(Integer produceRate) {
        double fearRate = (double) (currentKingdom.getFearRate()) / 10;
        produceRate += (int) ((double) produceRate * fearRate);
        produceRate *= (gameMap.getPlayers().size() - 1);
        return produceRate;
    }

    public void executeMines() {
        MineType mineType;
        Mine mine;
        for (Building building : currentKingdom.getBuildings())
            if (building instanceof Mine) {
                mineType = (MineType) building.getSpecificConstant();
                mine = (Mine) building;
                MapBlock mapBlock;
                if (mine.getMineMode().equals(ProduceMode.NON_ACTIVE)) continue;
                if ((mapBlock = checkNearBlocksForResource(building.getPosition(), mineType.getResourceType())) == null) continue;
                if (!checkMineProduce(mineType.getResourceType(), mapBlock, setProduceRate(mineType.getProduceRate()))) continue;
                currentKingdom.setResourceAmount(mineType.getResourceType(), setProduceRate(mineType.getProduceRate()));
                mapBlock.setResources(mineType.getResourceType(), mapBlock.getResourceAmount() - setProduceRate(mineType.getProduceRate()));
            }
    }

    public boolean checkMineProduce(ResourceType resourceType, MapBlock mapBlock, Integer produceRate) {
        if (mapBlock.getResourceAmount() - produceRate < 0) {
            produceResource(resourceType, mapBlock.getResourceAmount());
            mapBlock.setResources(resourceType, 0);
            return false;
        }
        if (currentKingdom.getResourceAmount(resourceType) + produceRate >
                currentKingdom.getNumberOfStock(BuildingType.STOCKPILE) * StockType.STOCKPILE.getCAPACITY()) {
            int amount = currentKingdom.getNumberOfStock(BuildingType.STOCKPILE) * StockType.STOCKPILE.getCAPACITY() -
                    currentKingdom.getResourceAmount(resourceType);
            produceResource(resourceType, amount);
            mapBlock.setResources(resourceType, mapBlock.getResourceAmount() - amount);
            return false;
        }
        return true;

    }
    public MapBlock checkNearBlocksForResource(MapBlock currentBlock, ResourceType resourceType) {
        int x  = currentBlock.getxPosition(), y = currentBlock.getyPosition();
        MapBlock mapBlock;
        for (int i = -1; i < 2; i++) {
            if ( i + x > gameMap.getMapWidth() || i + x < 0) continue;
            for (int j = -1; j < 2; j++) {
                if ( j + y  > gameMap.getMapHeight() || j + y < 0) continue;
                mapBlock = gameMap.getMapBlockByLocation(x + i, y + j);
                if (mapBlock.getResources().equals(resourceType) && mapBlock.getResourceAmount() > 0)
                    return mapBlock;
            }
        }
        currentKingdom.getUnits().removeAll(currentBlock.getUnitByUnitType(UnitType.WORKER));
        currentBlock.getUnits().removeAll(currentBlock.getUnitByUnitType(UnitType.WORKER));
        currentKingdom.setNoneEmployed(currentBlock.getUnitByUnitType(UnitType.WORKER).size());
        ((Mine) currentBlock.getBuildings()).setMineMode(ProduceMode.NON_ACTIVE);
        return null;
    }

    public void executeProducerBuilding() {
        for (Building building : currentKingdom.getBuildings())
            if (building instanceof Producer && !((Producer) building).getMode().equals(ProduceMode.NON_ACTIVE))
                checkProduce((Producer) building);
    }

    public void produceResource(ResourceType resourceType, Integer amount) {
        currentKingdom.setResourceAmount(resourceType, amount);
        if(resourceType.getBaseSource() != null)
            currentKingdom.setResourceAmount(resourceType.getBaseSource(), -amount);
    }

    public void produceWeapon(Weapons weapons, Integer amount) {
        currentKingdom.setWeaponsAmount(weapons, amount);
        currentKingdom.setResourceAmount(weapons.getResourceType(), -amount);
    }

    public void produceFood(Food food, Integer amount) {
        currentKingdom.setFoodsAmount(food, amount);
        currentKingdom.setResourceAmount(food.getResourceType(), -amount);
    }

    public void checkProduce(Producer producer) {
        ProducerType producerType = (ProducerType) producer.getSpecificConstant();
        Enum<?> check0, check1;
        Integer amount = 1;
        check0 = producerType.getTypeOfResource0();
        check1 = producerType.getTypeOfResource1();
        if (check0 != null && check1 != null && !producer.getMode().equals(ProduceMode.FIRST) && !producer.getMode().equals(ProduceMode.SECOND))
            amount = 2;
        if (checkCapacity(check0, setProduceRate(producerType.getProduceRate()) / amount)) {
            if (!producer.getMode().equals(ProduceMode.SECOND)) {
                if (check0 instanceof Weapons) produceWeapon((Weapons) check0, setProduceRate(producerType.getProduceRate()) / amount);
                else if (check0 instanceof Food) produceFood((Food) check0, setProduceRate(producerType.getProduceRate()) / amount);
                else if (check0 != null) produceResource((ResourceType) check0, setProduceRate(producerType.getProduceRate()) / amount);
            }
        }
        if (checkCapacity(check1, setProduceRate(producerType.getProduceRate()) / amount)) {
            if (!producer.getMode().equals(ProduceMode.FIRST) && check1 != null)
                produceWeapon((Weapons) check1, setProduceRate(producerType.getProduceRate()) / amount);
        }
    }


    public boolean checkFoodCapacity(Food food, Integer produceRate) {
        if (currentKingdom.getResourceAmount(food.getResourceType()) - produceRate < 0) {
            produceFood(food, currentKingdom.getResourceAmount(food.getResourceType()));
            return false;
        }
        if (currentKingdom.getFoodAmount(food) + produceRate >
                currentKingdom.getNumberOfStock(BuildingType.FOOD_STOCKPILE) * StockType.FOOD_STOCKPILE.getCAPACITY()) {
            produceFood(food, currentKingdom.getNumberOfStock(BuildingType.FOOD_STOCKPILE) *
                    StockType.FOOD_STOCKPILE.getCAPACITY() - currentKingdom.getFoodAmount(food));
            return false;
        }
        return true;
    }

    public boolean checkResourceCapacity(ResourceType resourceType, Integer produceRate) {
        if (currentKingdom.getResourceAmount(resourceType.getBaseSource()) - produceRate < 0) {
            produceResource(resourceType, currentKingdom.getResourceAmount(resourceType.getBaseSource()));
            return false;
        }
        if (currentKingdom.getResourceAmount(resourceType) + produceRate >
                currentKingdom.getNumberOfStock(BuildingType.STOCKPILE) * StockType.STOCKPILE.getCAPACITY()) {
            produceResource(resourceType, currentKingdom.getNumberOfStock(BuildingType.STOCKPILE) *
                    StockType.STOCKPILE.getCAPACITY() - currentKingdom.getResourceAmount(resourceType));
            return false;
        }
        return true;
    }

    public boolean checkWeaponCapacity(Weapons weapons, Integer produceRate) {
        if (currentKingdom.getResourceAmount(weapons.getResourceType()) - produceRate < 0) {
            produceWeapon(weapons, currentKingdom.getResourceAmount(weapons.getResourceType()));
            return false;
        }
        if (currentKingdom.getWeaponAmount(weapons) + produceRate >
                currentKingdom.getNumberOfStock(BuildingType.ARMOURY) * StockType.ARMOURY.getCAPACITY()) {
            produceWeapon(weapons, currentKingdom.getNumberOfStock(BuildingType.ARMOURY) *
                    StockType.ARMOURY.getCAPACITY() - currentKingdom.getWeaponAmount(weapons));
            return false;
        }
        return true;
    }

    public boolean checkCapacity(Enum<?> resource, Integer produceRate) {
        if (resource == null) return false;
        if (resource instanceof ResourceType) return checkResourceCapacity((ResourceType) resource, produceRate);
        else if (resource instanceof Food) return checkFoodCapacity((Food) resource, produceRate);
        else return checkWeaponCapacity((Weapons) resource, produceRate);
    }

    public void move() {
        for (Building building : currentKingdom.getRemainingBuildingMove())
            building.getPosition().setSiege(building);
        currentKingdom.getRemainingBuildingMove().clear();
        for (Unit unit : currentKingdom.getRemainingUnitMove())
            unit.getLocationBlock().addUnitHere(unit);
        currentKingdom.getRemainingUnitMove().clear();
    }

    public static void calculateResources(){

    }

    public static void removeDeadUnits(){

    }

    public static void removeDestroyedBuildings(){

    }

    public static void checkWinOrLose(){

    }

}
