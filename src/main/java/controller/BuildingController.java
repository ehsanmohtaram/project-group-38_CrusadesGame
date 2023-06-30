package controller;

import model.*;
import model.building.*;
import model.unit.Unit;
import model.unit.UnitState;
import model.unit.UnitType;
import java.util.HashMap;
import java.util.Objects;

public class BuildingController {
    private final Building selectedBuilding;
    private final Map gameMap;
    private final Kingdom currentKingdom;


    public BuildingController(Map gameMap, Building selectedBuilding) {
        this.selectedBuilding = selectedBuilding;
        this.gameMap = gameMap;
        currentKingdom = gameMap.getKingdomByOwner(Controller.currentUser);

    }

    public String repairBuilding() {
        if (currentKingdom.checkForAvailableNormalUnit(UnitType.ENGINEER) == 0) return "There is no available workers!";
        BuildingType buildingType = selectedBuilding.getBuildingType();
        if (currentKingdom.getResourceAmount(ResourceType.ROCK) < ((buildingType.getHP_IN_FIRST() - selectedBuilding.getHp()) / 200))
            return "You do not have enough resource!";
        for (Unit unit : selectedBuilding.getPosition().getUnits())
            if (!unit.getOwner().equals(currentKingdom))
                return "Enemies detected in the block!";
        int xPosition = selectedBuilding.getPosition().getxPosition();
        int yPosition = selectedBuilding.getPosition().getxPosition();
        for (int i = -2; i <= 2; i++)
            for (int j = -2; j <= 2; j++)
                for (Unit unit : gameMap.getMapBlockByLocation(xPosition + i, yPosition + j).getUnits())
                    if (!unit.getOwner().equals(currentKingdom))
                        return "Enemies detected around the block!";
        if (selectedBuilding.getHp().equals(buildingType.getHP_IN_FIRST())) return "Building hp is already full!";
        currentKingdom.setResourceAmount(ResourceType.ROCK, (buildingType.getHP_IN_FIRST() - selectedBuilding.getHp()) / 200);
        selectedBuilding.damage(selectedBuilding.getHp() - selectedBuilding.getBuildingType().getHP_IN_FIRST());
        return "done";
    }

    public void createUnitAdditional(UnitType unitType, int count) {
        Camp camp = (Camp) selectedBuilding;
        Unit unit;
        for (int i = 0; i < count; i++) {
            unit = new Unit(unitType, selectedBuilding.getPosition(), currentKingdom);
            camp.setUnits(unit);
            currentKingdom.addUnit(unit);
            if (!unitType.getIS_ARAB().equals(0) && !unitType.getIS_ARAB().equals(1))
                unit.setUnitState(UnitState.NOT_WORKING);
            else unit.setUnitState(UnitState.STANDING);
        }
        camp.setCapacity(count);
        if (unitType.getIS_ARAB().equals(1)) currentKingdom.setBalance((double) -unitType.getPRICE() * count);
        if (unitType.equals(UnitType.KNIGHT)) deleteHorseFromMap(count);
        if (unitType.getWEAPON_NEEDED() != null) currentKingdom.setWeaponsAmount(unitType.getWEAPON_NEEDED(), -count);
        if (unitType.getArmour_Needed() != null) currentKingdom.setWeaponsAmount(unitType.getArmour_Needed(), -count);
    }


    public String checkForKnightHorse(int count) {
        int horseCounter = 0;
        for (Building building : currentKingdom.getBuildings())
            if (building.getBuildingType().equals(BuildingType.STABLE))
                horseCounter += ((Camp) building).getCapacity();
        if (horseCounter < count) return "You do not have enough horses to make knight!";
        return "done";
    }

    public void deleteHorseFromMap(int count) {
        int marker = count;
        int horseCounter;
        Camp camp;
        for (Building building : currentKingdom.getBuildings()) {
            if (building.getBuildingType().equals(BuildingType.STABLE)) {
                camp = (Camp) building;
                if (marker <= camp.getCapacity()) horseCounter = count;
                else horseCounter = camp.getCapacity();
                marker -= horseCounter;
                int check = 0;
                while (check != horseCounter) {
                    camp.setCapacity(-1);
                    check++;
                }
            }
            if (marker == 0) break;
        }
    }

    public String createUnit(UnitType unitType) {
        String result;
        if (selectedBuilding.getBuildingType().equals(BuildingType.STABLE)) return "Invalid command";
//        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
//        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
//        try {UnitType.valueOf(options.get("t").toUpperCase().replaceAll(" ", "_"));}
//        catch (Exception ignored) {return "There is no such a unit found!";}
//        if (!options.get("c").matches("-?\\d+")) return "Please input a digit as count value!";
//        if (Integer.parseInt(options.get("c")) < 0 || Integer.parseInt(options.get("c")) > 20) return "Invalid bounds!";
//        UnitType unitType = UnitType.valueOf(options.get("t").toUpperCase().replaceAll(" ", "_"));
        CampType campType = (CampType) selectedBuilding.getBuildingType().specificConstant;
        if (!Objects.equals(campType.getIsArab(), unitType.getIS_ARAB())) return "You can not build this type of unit here!";
        int count = 1;
        if (unitType.getIS_ARAB().equals(1) && unitType.getPRICE() * count > currentKingdom.getBalance())
            return "You do not have enough balance to buy this unit!";
        if (currentKingdom.getBuildingFormKingdom(BuildingType.ARMOURY) == null && selectedBuilding.getBuildingType().equals(BuildingType.BARRACK))
            return "Please build armoury before creating units!";
        if (unitType.getWEAPON_NEEDED() != null)
            if (currentKingdom.getWeaponAmount(unitType.getWEAPON_NEEDED()) < count) return "You do not have enough weapon!";
        if (unitType.getArmour_Needed() != null)
            if (currentKingdom.getWeaponAmount(unitType.getArmour_Needed()) < count) return "You do not have enough weapon!";
        if (unitType.equals(UnitType.KNIGHT)) {
            result = checkForKnightHorse(count); if (!result.equals("done")) return result;
        }
        Camp camp = (Camp) selectedBuilding;
        if (campType.getCapacity() < camp.getCapacity() + count) return "Your camp is full. Please make a new camp!";
        if (currentKingdom.getNoneEmployed() < count)
            return "You do not have enough population to make new units!";
        createUnitAdditional(unitType, count);
        return "successful";
    }

    public String setMode(HashMap<String, String> options) {
        String result;
        ProduceMode.valueOf(options.get("m"));
        ProduceMode produceMode = ProduceMode.valueOf(options.get("m"));
        Producer producer = (Producer) selectedBuilding;
        result = checkProduceMode();
        if (result != null) return result;
        producer.setMode(produceMode);
        return null;
    }

    public String checkProduceMode() {
        Producer producer = (Producer) selectedBuilding;
        ProducerType producerType = (ProducerType) producer.getSpecificConstant();
        Enum<?> check0, check1;
        check0 = producerType.getTypeOfResource0();
        check1 = producerType.getTypeOfResource1();
        if (!producer.getMode().equals(ProduceMode.SECOND)) {
            if (check0 instanceof Weapons && currentKingdom.getBuildingFormKingdom(BuildingType.ARMOURY) == null)
                return "You should build armoury first!";
            else if (check0 instanceof Food && currentKingdom.getBuildingFormKingdom(BuildingType.FOOD_STOCKPILE) == null)
                return "You should build food stock first!";
        }
        if (!producer.getMode().equals(ProduceMode.FIRST) && check1 != null)
            if (currentKingdom.getBuildingFormKingdom(BuildingType.ARMOURY) == null)
                return "You should build armoury first!";
        return null;
    }

    public String checkType(String type) {
        try {
            ResourceType.valueOf(type);
            return "resource";
        } catch (Exception ignored) {
        }
        try {
            Weapons.valueOf(type);
            return "weapon";
        } catch (Exception ignored) {
        }
        try {
            Food.valueOf(type);
            return "food";
        } catch (Exception ignored) {
        }
        return null;
    }

    public String buyFromShop(String options) {
        int amount = 1;
        String result = checkType(options);
        ResourceType resourceType;
        Weapons weapons;
        Food food;
        switch (result) {
            case "food":
                food = Food.valueOf(options);
                if (food.getPrice() * amount > currentKingdom.getBalance()) return "You do not have enough balance!";
                if (currentKingdom.getBuildingFormKingdom(BuildingType.FOOD_STOCKPILE) == null)
                    return "You do not have food stockpile!";
                if (currentKingdom.getNumberOfStock(BuildingType.FOOD_STOCKPILE) * StockType.FOOD_STOCKPILE.getCAPACITY() <
                        currentKingdom.getFoodAmount(food) + amount)
                    return "You do not have enough space!";
                currentKingdom.setFoodsAmount(food, amount);
                currentKingdom.setBalance((double) -food.getPrice());
                break;
            case "weapon":
                weapons = Weapons.valueOf(options);
                if (weapons.getCost() * amount > currentKingdom.getBalance()) return "You do not have enough balance!";
                if (currentKingdom.getBuildingFormKingdom(BuildingType.ARMOURY) == null)
                    return "You do not have armoury!";
                if (currentKingdom.getNumberOfStock(BuildingType.ARMOURY) * StockType.ARMOURY.getCAPACITY() <
                        currentKingdom.getWeaponAmount(weapons) + amount)
                    return "You do not have enough space!";
                currentKingdom.setWeaponsAmount(weapons, amount);
                currentKingdom.setBalance((double) -weapons.getCost());
                break;
            case "resource":
                resourceType = ResourceType.valueOf(options);
                if (resourceType.getPrice() * amount > currentKingdom.getBalance())
                    return "You do not have enough balance!";
                if (currentKingdom.getNumberOfStock(BuildingType.STOCKPILE) * StockType.STOCKPILE.getCAPACITY() <
                        currentKingdom.getResourceAmount(resourceType) + amount)
                    return "You do not have enough space!";
                currentKingdom.setResourceAmount(resourceType, amount);
                currentKingdom.setBalance((double) -resourceType.getPrice());
                break;
        }
        return "done";
    }

    public String sellFromShop(String options) {
        int amount = 1;
        String result = checkType(options);
        ResourceType resourceType;
        Weapons weapons;
        Food food;
        switch (result) {
            case "food":
                food = Food.valueOf(options);
                if (currentKingdom.getFoodAmount(food) < amount) return "You do not have enough foods!";
                currentKingdom.setFoodsAmount(food, -amount);
                currentKingdom.setBalance(food.getPrice() * 0.8);
                break;
            case "weapon":
                weapons = Weapons.valueOf(options);
                if (currentKingdom.getWeaponAmount(weapons) < amount) return "You do not have enough weapon!";
                currentKingdom.setWeaponsAmount(weapons, -amount);
                currentKingdom.setBalance(weapons.getCost() * 0.8);
                break;
            case "resource":
                resourceType = ResourceType.valueOf(options);
                if (currentKingdom.getResourceAmount(resourceType) < amount) return "You do not have enough resource!";
                currentKingdom.setResourceAmount(resourceType, -amount);
                currentKingdom.setBalance(resourceType.getPrice() * 0.8);
                break;
        }
        return "done";
    }

    public String positionValidate(String xPosition, String yPosition) {
        if (!xPosition.matches("-?\\d+") && !yPosition.matches("-?\\d+"))
            return "Please input digit as your input values!";
        if (Integer.parseInt(xPosition) > gameMap.getMapWidth() ||
                Integer.parseInt(yPosition) > gameMap.getMapWidth() ||
                Integer.parseInt(xPosition) < 0 ||
                Integer.parseInt(yPosition) < 0) return "Invalid bounds!";
        return null;
    }

    public String moveSiege(HashMap<String, String> options) {
        if (!(selectedBuilding instanceof Siege)) return "Invalid command";
        SiegeType siegeType = (SiegeType) selectedBuilding.getSpecificConstant();
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet())
            if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        String position = positionValidate(options.get("x"), options.get("y"));
        if (position != null) return position;
        MapBlock goingPosition = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")), Integer.parseInt(options.get("y")));
        if (!siegeType.getPortable()) return "This siege is not movable!";
        if (!goingPosition.getMapBlockType().isAccessible()) return "You can not go to this block!";
        if (goingPosition.getBuildings() != null || goingPosition.getSiege() != null)
            return "This block is already filled with another building!";
        if (gameMap.getShortestWayLength(selectedBuilding.getPosition().getxPosition(), selectedBuilding.getPosition().getyPosition()
                , goingPosition.getxPosition(), goingPosition.getyPosition(), siegeType.getMoveRange()) == null)
            return "This siege is too slow to reach such destination!";
        int counter = 0;
        while (selectedBuilding.getBuildingType().getNumberOfWorker() != counter && selectedBuilding.getPosition().getUnits().size() != 0) {
            for (Unit unit : selectedBuilding.getPosition().getUnits())
                if (unit.getUnitType().equals(UnitType.ENGINEER) && unit.getUnitState().equals(UnitState.WORKING) && unit.getOwner().equals(currentKingdom)) {
                    unit.setLocationBlock(goingPosition);
                    currentKingdom.setRemainingUnitMove(unit);
                    selectedBuilding.getPosition().removeUnitFromHere(unit);
                    counter++;
                    break;
                }
        }
        selectedBuilding.getPosition().getUnits().get(0).setLocationBlock(goingPosition);
        currentKingdom.setRemainingUnitMove(selectedBuilding.getPosition().getUnits().get(0));
        selectedBuilding.getPosition().setSiege(null);
        selectedBuilding.getPosition().setSiege(null);
        selectedBuilding.setPosition(goingPosition);
        currentKingdom.setRemainingBuildingMove(selectedBuilding);
        return "Your siege moved successfully!";
    }

    public MapBlock checkBridgePosition(MapBlock currentBlock) {
        int x = currentBlock.getxPosition();
        int y = currentBlock.getyPosition();
        MapBlock mapBlock;
        for (int i = -1; i <= 1; i++) {
            if (i + x > gameMap.getMapWidth() || i + x < 0) continue;
            for (int j = -1; j <= 1; j++) {
                if (j == i || (j + i == 0)) continue;
                if (j + y > gameMap.getMapHeight() || j + y < 0) continue;
                mapBlock = gameMap.getMapBlockByLocation(x + i, y + j);
                if (mapBlock.getBuildings() != null && (mapBlock.getBuildings().getBuildingType().equals(BuildingType.SMALL_STONE_GATEHOUSE) ||
                        mapBlock.getBuildings().getBuildingType().equals(BuildingType.BIG_STONE_GATEHOUSE))) {
                    if (gameMap.getGateDirection(mapBlock.getBuildings()).equals(Direction.NORTH) && (x == (x + i)))
                        return mapBlock;
                    if (gameMap.getGateDirection(mapBlock.getBuildings()).equals(Direction.EAST) && (y == (y + j)))
                        return mapBlock;
                }
            }
        }
        return null;
    }

    public String openAccess(String access) {
        boolean check;
        check = access.equals("open");
        if (!currentKingdom.getFlag().equals(gameMap.getGateFlag(selectedBuilding)))
            return "You can not open in this gate!";
        changeAccess(check);
        return "done";
    }

    public void changeAccess(boolean check) {
        MapBlock mapBlock;
        if (selectedBuilding.getBuildingType().equals(BuildingType.DRAWBRIDGE)) {
            mapBlock = checkBridgePosition(selectedBuilding.getPosition());
            if (gameMap.getGateDirection(mapBlock.getBuildings()).equals(Direction.NORTH)) {
                gameMap.changeAccess(selectedBuilding.getPosition().getxPosition(), selectedBuilding.getPosition().getyPosition(), Direction.NORTH, check);
                gameMap.changeAccess(selectedBuilding.getPosition().getxPosition(), selectedBuilding.getPosition().getyPosition(), Direction.SOUTH, check);
            }
            else {
                gameMap.changeAccess(selectedBuilding.getPosition().getxPosition(), selectedBuilding.getPosition().getyPosition(), Direction.EAST, check);
                gameMap.changeAccess(selectedBuilding.getPosition().getxPosition(), selectedBuilding.getPosition().getyPosition(), Direction.WEST, check);
            }
        }
        else {
            if (gameMap.getGateDirection(selectedBuilding).equals(Direction.NORTH)) {
                gameMap.changeAccess(selectedBuilding.getPosition().getxPosition(), selectedBuilding.getPosition().getyPosition(), Direction.NORTH, check);
                gameMap.changeAccess(selectedBuilding.getPosition().getxPosition(), selectedBuilding.getPosition().getyPosition(), Direction.SOUTH, check);
            }
            else {
                gameMap.changeAccess(selectedBuilding.getPosition().getxPosition(), selectedBuilding.getPosition().getyPosition(), Direction.EAST, check);
                gameMap.changeAccess(selectedBuilding.getPosition().getxPosition(), selectedBuilding.getPosition().getyPosition(), Direction.WEST, check);
            }
        }
    }

    public boolean getAccessType() {
        int counter = 0;
        for (Direction direction : Direction.values())
            if (gameMap.checkAccess(selectedBuilding.getPosition().getxPosition(), selectedBuilding.getPosition().getyPosition(),direction)) {
                counter ++;
                if (counter == 2) return true;
            }
        return false;
    }

    public String attackOnUnit(HashMap<String, String> options) {
        String result;
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (selectedBuilding.getBuildingType().equals(BuildingType.MANTLET)) return "You can not attack with siege!";
        result = positionValidate(options.get("x"), options.get("y"));
        Unit currentUnit = selectedBuilding.getPosition().getUnits().get(0);
        if (result != null) return result;
        MapBlock target = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if(target == null) return "Invalid location";
        if(target.getUnits().size() == 0 && target.getBuildings() == null) return "No enemy detected there!";
        if (target.getOptimizedDistanceFrom(selectedBuilding.getPosition().getxPosition(),
                selectedBuilding.getPosition().getyPosition(), false) > currentUnit.getOptimizedAttackRange())
            return "Too far to attack!";
        Building building = target.getBuildings();
        if (target.getUnits().size() != 0 && !selectedBuilding.getBuildingType().equals(BuildingType.BATTERING_RAM))
            for (Unit unit : target.getUnits()) currentUnit.unilateralFight(unit);
        if (target.getBuildings() != null)
            building.decreaseHP(currentUnit.getOptimizedDamage() * 10);
        return "battle done!";
    }

}
