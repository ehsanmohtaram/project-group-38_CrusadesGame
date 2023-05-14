package controller;
import model.*;
import model.building.*;
import model.unit.Unit;
import model.unit.UnitState;
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
            innCheck();
            produceHorse();
        }
        setReligiousBuildingPopularity();
        giveFood();
        getTax();
        move();
        removeUnitsAndBuildingWith0Hp();
        growPopulation();
        battleExecution();
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
        int produceRate;
        for (Building building : currentKingdom.getBuildings()) {
            if (building instanceof Mine) {
                mineType = (MineType) building.getSpecificConstant();
                mine = (Mine) building;
                MapBlock mapBlock;
                System.out.println(mine.getMineMode());
                if (mine.getMineMode().equals(ProduceMode.NON_ACTIVE)) continue;
                produceRate = setProduceRate(mineType.getProduceRate());
                if (mine.getSpecificConstant().equals(MineType.QUARRY))
                    produceRate = setProduceRate(MineType.OX_TETHER.getProduceRate());
                if (!mine.getSpecificConstant().equals(MineType.OX_TETHER)) {
                    if (mineType.equals(MineType.QUARRY) && currentKingdom.getBuildingFormKingdom(BuildingType.OX_TETHER) == null)
                        continue;
                    if ((mapBlock = checkNearBlocksForResource(building.getPosition(), mineType.getResourceType())) == null)
                        continue;
                    if (!checkMineProduce(mineType.getResourceType(), mapBlock, produceRate)) continue;
                    if (mineType.equals(MineType.QUARRY)) {
                        if (findNearestOX(building.getPosition()) != null) {
                            if (calculateCowsMoving((Mine) findNearestOX(building.getPosition()).getBuildings(), (Mine) building)) {
                                mapBlock.setResources(mineType.getResourceType(), mapBlock.getResourceAmount() - produceRate);
                                currentKingdom.setResourceAmount(mineType.getResourceType(), produceRate);
                            }
                        }
                    } else {
                        mapBlock.setResources(mineType.getResourceType(), mapBlock.getResourceAmount() - produceRate);
                        currentKingdom.setResourceAmount(mineType.getResourceType(), produceRate);
                    }
                }
            }
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

    public MapBlock findNearestOX(MapBlock mapBlock) {
        int min = gameMap.getMapWidth() * gameMap.getMapWidth() + gameMap.getMapHeight() * gameMap.getMapHeight();
        MapBlock minDistanceToOx = null;
        for (Building building : currentKingdom.getBuildings())
            if (building.getBuildingType().equals(BuildingType.OX_TETHER))
                if (min > (mapBlock.getxPosition() - building.getPosition().getxPosition()) * (mapBlock.getxPosition() - building.getPosition().getxPosition()) +
                        (mapBlock.getyPosition() - building.getPosition().getyPosition()) * (mapBlock.getyPosition() - building.getPosition().getyPosition())) {
                    min = (mapBlock.getxPosition() - building.getPosition().getxPosition()) * (mapBlock.getxPosition() - building.getPosition().getxPosition()) +
                            (mapBlock.getyPosition() - building.getPosition().getyPosition()) * (mapBlock.getyPosition() - building.getPosition().getyPosition());
                    minDistanceToOx = building.getPosition();
                }
        return minDistanceToOx;
    }

    public boolean calculateCowsMoving(Mine oxPosition, Mine query) {
        int distance;
        int x1 = oxPosition.getPosition().getxPosition();
        int x2 = query.getPosition().getxPosition();
        int y1 = oxPosition.getPosition().getyPosition();
        int y2 = query.getPosition().getyPosition();
        distance = (int) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
        if (currentKingdom.getMoveOfCows(oxPosition) <= 0) {currentKingdom.setMoveOfCows(oxPosition, distance); return true;}
        else {currentKingdom.setMoveOfCows(oxPosition, currentKingdom.getMoveOfCows(oxPosition) - 2); return false;}
    }

    public void move() {
        for (Building building : currentKingdom.getRemainingBuildingMove())
            building.getPosition().setSiege(building);
        currentKingdom.getRemainingBuildingMove().clear();
        for (Unit unit : currentKingdom.getRemainingUnitMove()) {
            if (unit.getUnitType().getIS_ARAB().equals(-4))
                unit.getLocationBlock().getUnits().set(0, unit);
        }
        currentKingdom.getRemainingUnitMove().clear();
    }

    public void removeUnitsAndBuildingWith0Hp() {
        for (MapBlock[] mapBlock : gameMap.getMap()) {
            for (MapBlock checkBlock : mapBlock) {
                removeDestroyedUnits(checkBlock);
                removeDeadUnits(checkBlock);
                removeSiegeUnits(checkBlock);
            }
        }
    }

    public void removeDestroyedUnits(MapBlock checkBlock) {
        Kingdom owner;
        if (checkBlock.getBuildings() != null) {
            owner = checkBlock.getBuildings().getOwner();
            if (checkBlock.getBuildings().getHp() <= 0) {
                if (checkBlock.getBuildings() instanceof Stock) {
                    for (Enum<?> resource : ((Stock)checkBlock.getBuildings()).getResourceValues().keySet()) {
                        if (resource instanceof Food)
                            owner.setFoodsAmount((Food) resource, -((Stock)checkBlock.getBuildings()).getResourceValues().get(resource));
                        else if (resource instanceof Weapons)
                            owner.setWeaponsAmount((Weapons) resource, -((Stock)checkBlock.getBuildings()).getResourceValues().get(resource));
                        else owner.setResourceAmount((ResourceType) resource, -((Stock)checkBlock.getBuildings()).getResourceValues().get(resource));
                    }
                }
                checkBlock.getBuildings().getOwner().getBuildings().remove(checkBlock.getBuildings());
                checkBlock.setBuildings(null);
            }
        }
    }

    public void removeDeadUnits(MapBlock checkBlock) {
        if (checkBlock.getUnits().size() != 0) {
            for (Unit unit : checkBlock.getUnits()) {
                if (unit.getHp() <= 0) {
                    unit.getOwner().getUnits().remove(unit);
                    checkBlock.removeUnitFromHere(unit);
                }
            }
        }
    }

    public void removeSiegeUnits(MapBlock checkBlock) {
        Building building;
        int counter = 0;
        if (checkBlock.getSiege() != null) {
            building = checkBlock.getSiege();
            if (checkBlock.getSiege().getHp() <= 0) {
                while (building.getBuildingType().getNumberOfWorker() != counter || building.getPosition().getUnits().size() != 0) {
                    for (Unit findUnit : checkBlock.getUnits())
                        if (findUnit.getUnitType().equals(UnitType.ENGINEER)) {
                            building.getPosition().removeUnitFromHere(findUnit);
                            findUnit.getOwner().setNoneEmployed(1);
                            break;
                        }
                    counter++;
                }
                checkBlock.getSiege().getOwner().getBuildings().remove(checkBlock.getBuildings());
                checkBlock.setSiege(null);
            }
        }
    }

    public void battleExecution(){
        for (Unit aggressiveUnit : Unit.AggressiveUnits)
            aggressiveUnitsFight(aggressiveUnit);
        for (Kingdom player : gameMap.getPlayers()) {
            for (Unit unit : player.getUnits()) {
                if(unit.getUnitState().equals(UnitState.DEFENSIVE)) {
                    Unit nearestEnemy = gameMap.getEnemiesInAttackRange(unit, true).get(0);
                    if(nearestEnemy.getUnitState().equals(UnitState.OFFENSIVE))
                        unit.bilateralFight(nearestEnemy, true);
                    else
                        unit.unilateralFight(nearestEnemy);
                }
            }
        }
    }

    private void aggressiveUnitsFight(Unit unit){
        for (Unit defender : gameMap.getEnemiesInAttackRange(unit, true)) {
            unit.moveTo(defender.getLocationBlock(), 0);
            if(!unit.bilateralFightTillEnd(defender))
                return;
        }
        unit.setUnitState(UnitState.STANDING);
    }

}
