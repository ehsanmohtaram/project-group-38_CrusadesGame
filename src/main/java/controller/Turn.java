package controller;
import model.*;
import model.building.*;
import model.unit.UnitType;

public class Turn {

    private final Map gameMap;
    private final Kingdom currentKingdom;
    private final User currentUser;
    private final Kingdom lastKingdom;

    public Turn() {
        gameMap = GameController.gameMap;
        currentUser = Controller.currentUser;
        currentKingdom = gameMap.getKingdomByOwner(currentUser);
        int result = ((gameMap.getPlayers().indexOf(currentKingdom) - 1) + gameMap.getPlayers().size()) % gameMap.getPlayers().size();
        lastKingdom = gameMap.getPlayers().get(result);
    }

    public void runNextTurn() {
        executeProducerBuilding();
        executeMines();
    }

    private void executeMines() {
        MineType mineType;
        Mine mine;
        for (Building building : currentKingdom.getBuildings())
            if (building instanceof Mine) {
                mineType = (MineType) building.getSpecificConstant();
                mine = (Mine) building;
                MapBlock mapBlock;
                if (mine.getMineMode().equals(ProduceMode.NON_ACTIVE)) continue;
                if ((mapBlock = checkNearBlocksForResource(building.getPosition(), mineType.getResourceType())) == null) continue;
                if (!checkMineProduce(mineType.getResourceType(), mapBlock, mineType.getProduceRate())) continue;
                currentKingdom.setResourceAmount(mineType.getResourceType(), mineType.getProduceRate());
                mapBlock.setResources(mineType.getResourceType(), mapBlock.getResourceAmount() - mineType.getProduceRate());
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


    private void executeProducerBuilding() {
        for (Building building : currentKingdom.getBuildings())
            if (building instanceof Producer && !((Producer) building).getMode().equals(ProduceMode.NON_ACTIVE))
                checkProduce((Producer) building);
    }

    private void produceResource(ResourceType resourceType, Integer amount) {
        currentKingdom.setResourceAmount(resourceType, amount);
        if(resourceType.getBaseSource() != null)
            currentKingdom.setResourceAmount(resourceType.getBaseSource(), -amount);
    }

    private void produceWeapon(Weapons weapons, Integer amount) {
        currentKingdom.setWeaponsAmount(weapons, amount);
        currentKingdom.setResourceAmount(weapons.getResourceType(), -amount);
    }

    private void produceFood(Food food, Integer amount) {
        currentKingdom.setFoodsAmount(food, amount);
        currentKingdom.setResourceAmount(food.getResourceType(), -amount);
    }

    private void checkProduce(Producer producer) {
        ProducerType producerType = (ProducerType) producer.getSpecificConstant();
        Enum<?> check0, check1;
        Integer amount = 1;
        check0 = producerType.getTypeOfResource0();
        check1 = producerType.getTypeOfResource1();
        if (check0 != null && check1 != null && !producer.getMode().equals(ProduceMode.FIRST) && !producer.getMode().equals(ProduceMode.SECOND))
            amount = 2;
        if (checkCapacity(check0, producerType.getProduceRate() / amount)) {
            if (!producer.getMode().equals(ProduceMode.SECOND)) {
                if (check0 instanceof Weapons) produceWeapon((Weapons) check0, producerType.getProduceRate() / amount);
                else if (check0 instanceof Food) produceFood((Food) check0, producerType.getProduceRate() / amount);
                else if (check0 != null) produceResource((ResourceType) check0, producerType.getProduceRate() / amount);
            }
        }
        if (checkCapacity(check1, producerType.getProduceRate() / amount)) {
            if (!producer.getMode().equals(ProduceMode.FIRST) && check1 != null)
                produceWeapon((Weapons) check1, producerType.getProduceRate() / amount);
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

    public static void calculateResources(){

    }

    public static void removeDeadUnits(){

    }

    public static void removeDestroyedBuildings(){

    }

    public static void checkWinOrLose(){

    }

}
