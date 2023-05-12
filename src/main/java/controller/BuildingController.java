package controller;

import model.*;
import model.building.*;
import model.unit.Unit;
import model.unit.UnitState;
import model.unit.UnitType;
import view.BuildingMenu;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Objects;

public class BuildingController {
    private final BuildingMenu buildingMenu;
    private final Building selectedBuilding;
    private final Map gameMap;
    private final Kingdom currentKingdom;


    public BuildingController() {
        buildingMenu = new BuildingMenu(this);
        selectedBuilding = GameController.selectedBuilding;
        gameMap = GameController.gameMap;
        currentKingdom = gameMap.getKingdomByOwner(Controller.currentUser);
    }

    public void run() {
        buildingMenu.mainBuildingClassRun();
    }

    public String redirect() {
        if (selectedBuilding.getSpecificConstant() instanceof DefensiveStructureType) return buildingMenu.defensiveBuildingRnu();
        else if (selectedBuilding.getSpecificConstant() instanceof CampType) return buildingMenu.campBuildingRnu();
        else if (selectedBuilding.getSpecificConstant() instanceof StockType) return buildingMenu.stockBuildingRun();
        else if (selectedBuilding.getSpecificConstant() instanceof ProducerType) return buildingMenu.produceBuildingRun();
        else if (selectedBuilding.getSpecificConstant() instanceof SiegeType) return buildingMenu.siegeRun();
        else if (selectedBuilding.getBuildingType().equals(BuildingType.SHOP)) return buildingMenu.runShop();
        else return null;
    }

    public String buildingName() {
        return "Building Name : " + selectedBuilding.getBuildingType().name().toLowerCase().replaceAll("_", " ");
    }

    public String buildingHp() {
        return "Hp : " + selectedBuilding.getHp() + " / " + selectedBuilding.getBuildingType().getHP_IN_FIRST();
    }

    public String repairBuilding() {
        if (selectedBuilding.getBuildingType().equals(BuildingType.CATHEDRAL) ||
            selectedBuilding.getBuildingType().equals(BuildingType.CHURCH)) return "Invalid command";
        if (currentKingdom.checkForAvailableNormalUnit(UnitType.ENGINEER) == 0) return "There is no available workers";
        BuildingType buildingType = selectedBuilding.getBuildingType();
        if (buildingType.getRESOURCE_NUMBER() > currentKingdom.getResourceAmount(buildingType.getRESOURCES()))
            return "You do not have enough " + buildingType.getRESOURCES().name().toLowerCase() + " to buy this building.";
        for (Unit unit : selectedBuilding.getPosition().getUnits())
            if (!unit.getOwner().equals(currentKingdom))
                return "This block should be free of soldier enemies while building is being repaid.";
        int xPosition = selectedBuilding.getPosition().getxPosition();
        int yPosition = selectedBuilding.getPosition().getxPosition();
        for (int i = -2; i <= 2; i++)
            for (int j = -2; j <= 2; j++)
                for (Unit unit : gameMap.getMapBlockByLocation(xPosition + i, yPosition + j).getUnits())
                    if (!unit.getOwner().equals(currentKingdom))
                        return "Blocks that neat the building should be free of enemies troop!";
        if (selectedBuilding.getHp().equals(buildingType.getHP_IN_FIRST())) return "Building hp is full.";
        selectedBuilding.damage(selectedBuilding.getHp() - selectedBuilding.getBuildingType().getHP_IN_FIRST());
        return "Building repaired successfully!";
    }
    public void createUnitAdditional(UnitType unitType, int count) {
        Camp camp = (Camp) selectedBuilding;
        Unit unit;
        for (int i = 0; i < count ;i++) {
            unit = new Unit(unitType, selectedBuilding.getPosition(), currentKingdom);
            camp.setUnits(unit);
            currentKingdom.addUnit(unit);
            if (!unitType.getIS_ARAB().equals(0) && !unitType.getIS_ARAB().equals(1)) unit.setUnitState(UnitState.NOT_WORKING);
            else unit.setUnitState(UnitState.STANDING);
        }
        camp.setCapacity(count);
        if (unitType.getIS_ARAB().equals(1)) currentKingdom.setBalance((double) -unitType.getPRICE() * count );
        if (!unitType.equals(UnitType.BLACK_MONK)) currentKingdom.setNoneEmployed(-count);
        else deleteMonkFromMap(count);
        if (unitType.equals(UnitType.KNIGHT)) deleteHorseFromMap(count);
        if (unitType.getWEAPON_NEEDED() != null) currentKingdom.setWeaponsAmount(unitType.getWEAPON_NEEDED(), -count);
        if (unitType.getArmour_Needed() != null) currentKingdom.setWeaponsAmount(unitType.getArmour_Needed(), -count);
    }

    public void deleteMonkFromMap(int count) {
        int marker = count;
        int monkCounter;
        for (Building building : currentKingdom.getBuildings()) {
            if (building.getBuildingType().equals(BuildingType.CHURCH)) {
                if (marker <= building.getPosition().getUnits().size()) monkCounter = count;
                else monkCounter = building.getPosition().getUnits().size();
                marker -= monkCounter;
                int check = 0;
                while (check != monkCounter) {
                    currentKingdom.getUnits().remove(building.getPosition().getUnits().get(0));
                    building.getPosition().getUnits().remove(0);
                    ((Camp) building).setCapacity(-1);
                    ((Camp) building).getUnits().remove(0);
                    check++;
                }
            }
            if (marker == 0) break;
        }
    }

    public String checkCathedral(int count) {
        int monkCounter = 0;
        for (Building building : currentKingdom.getBuildings()) {
            if (building.getBuildingType().equals(BuildingType.CHURCH))
                monkCounter += building.getPosition().getUnits().size();
        }
        if (monkCounter < count) return "You do not have enough monk to make black monks!";
        return "done";
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

    public String createUnit(HashMap<String, String> options) {
        String result;
        if (selectedBuilding.getBuildingType().equals(BuildingType.STABLE)) return "Invalid command";
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        try {UnitType.valueOf(options.get("t").toUpperCase().replaceAll(" ", "_"));}
        catch (Exception ignored) {return "There is no such a unit found!";}
        if (!options.get("c").matches("-?\\d+")) return "Please input a digit as count value!";
        if (Integer.parseInt(options.get("c")) < 0 || Integer.parseInt(options.get("c")) > 20 ) return "Invalid bounds!";
        UnitType unitType = UnitType.valueOf(options.get("t").toUpperCase().replaceAll(" ", "_"));
        CampType campType = (CampType) selectedBuilding.getBuildingType().specificConstant;
        if (!Objects.equals(campType.getIsArab(), unitType.getIS_ARAB())) return "You can not build this type of unit here!";
        int count = Integer.parseInt(options.get("c"));
        if (unitType.getIS_ARAB().equals(1) && unitType.getPRICE() * count > currentKingdom.getBalance())
            return "You do not have enough balance to buy this unit!";
        if (currentKingdom.getBuildingFormKingdom(BuildingType.ARMOURY) == null && selectedBuilding.getBuildingType().equals(BuildingType.BARRACK))
            return "Please build armoury before creating units!";
        if (unitType.getWEAPON_NEEDED() != null)
            if (currentKingdom.getWeaponAmount(unitType.getWEAPON_NEEDED()) < count) return "You do not have enough weapon!";
        if (unitType.getArmour_Needed() != null)
            if (currentKingdom.getWeaponAmount(unitType.getArmour_Needed()) < count) return "You do not have enough weapon!";
        if (unitType.equals(UnitType.KNIGHT)) {
            result = checkForKnightHorse(count);
            if (!result.equals("done")) return result;
        }
        Camp camp = (Camp) selectedBuilding;
        if (campType.getCapacity() < camp.getCapacity() + count) return "Your camp is full. Please make a new camp!";
        if (campType.equals(CampType.CATHEDRAL)) {
            result = checkCathedral(count);
            if (!result.equals("done")) return result;
        }
        else if (currentKingdom.getNoneEmployed() < count) return "You do not have enough population to make new units!";
        createUnitAdditional(unitType, count);
        return count + " " + unitType.name().toLowerCase().replaceAll("_"," ") + " has been made!";
    }

    public String setMode(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        String result;
        try {ProduceMode.valueOf(options.get("m").toUpperCase().replaceAll(" ", "_"));}
        catch (Exception ignored) {return "There is no such mode to change to it!";}
        ProduceMode produceMode = ProduceMode.valueOf(options.get("m").toUpperCase().replaceAll(" ", "_"));
        Producer producer = (Producer) selectedBuilding;
        result = checkProduceMode();
        if (result != null) return result;
        producer.setMode(produceMode);
        return "Produce mode has been changed successfully!";
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
        if (producer.getMode().equals(ProduceMode.SECOND) && check1 == null)
            return "There is only one product exist in this building!";
        if (!producer.getMode().equals(ProduceMode.FIRST) && check1 != null)
            if (currentKingdom.getBuildingFormKingdom(BuildingType.ARMOURY) == null)
                return "You should build armoury first!";
        return null;
    }


    public String showResources() {
        Stock stock = (Stock) selectedBuilding;
        StringBuilder stringBuilder = new StringBuilder();
        for (Enum<?> showResources : stock.getResourceValues().keySet())
            stringBuilder.append(showResources.name().toLowerCase().replaceAll("_", " ")).append(" : ").
                    append(stock.getResourceValues().get(showResources)).append("\n");
        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }

    private String showFood() {
        StringBuilder output = new StringBuilder("\nFood:");
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        for (Food value : Food.values()) {
            output.append("\nname: ").append(value.name().toLowerCase())
                    .append("   \t\t  buyPrice: ").append(decimalFormat.format(value.getPrice()))
                    .append("   sellPrice: ").append(decimalFormat.format(value.getPrice() * (0.8)))
                    .append("   amount: ").
                    append(currentKingdom.getFoodAmount(value));
        }
        return output.toString();
    }

    private String showResource() {
        StringBuilder output = new StringBuilder("Resource Types:");
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        for (ResourceType value : ResourceType.values()) {
            output.append("\nname: ").append(value.name().toLowerCase());
            if(value.name().equalsIgnoreCase("leather"))
                output.append("  \t\t  buyPrice: ").append(decimalFormat.format(value.getPrice()));
            else output.append("   \t\t  buyPrice: ").append(decimalFormat.format(value.getPrice()));
            output.append("   sellPrice: ").append(decimalFormat.format(value.getPrice() * (0.8)))
                    .append("   amount: ").append(currentKingdom.getResourceAmount(value));
        }
        return output.toString();
    }

    private String showWeapon() {
        StringBuilder output = new StringBuilder("\nWeapons:");
        DecimalFormat decimalFormat = new DecimalFormat("0.0");
        for (Weapons value : Weapons.values()) {
            output.append("\nname: ").append(value.name().toLowerCase());
            if(value.name().equalsIgnoreCase("leather_armour"))
                output.append("  buyPrice: ").append(decimalFormat.format(value.getCost()));
            else if (value.name().contains("_")) output.append("\t  buyPrice: ").append(decimalFormat.format(value.getCost()));
            else if (value.name().equalsIgnoreCase("crossbow"))
                output.append("\t\t  buyPrice: ").append(decimalFormat.format(value.getCost()));
            else output.append("   \t\t  buyPrice: ").append(decimalFormat.format(value.getCost()));
            output.append("  sellPrice: ").append(decimalFormat.format(value.getCost() * (0.8)))
                    .append("  amount: ").append(currentKingdom.getWeaponAmount(value));
        }
        return output.toString();

    }

    public String showPriceList() {
        return showResource() + showFood() + showWeapon();
    }

    public String checkType(String type) {
        try {ResourceType.valueOf(type.toUpperCase().replaceAll(" ","_")); return "resource";}
        catch (Exception ignored) {}
        try {Weapons.valueOf(type.toUpperCase().replaceAll(" ","_")); return "weapon";}
        catch (Exception ignored) {}
        try {Food.valueOf(type.toUpperCase().replaceAll(" ","_")); return "food";}
        catch (Exception ignored) {}
        return null;
    }

    public String buyFromShop(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (!options.get("a").matches("-?\\d+")) return "Please input digit as amount value!";
        int amount = Integer.parseInt(options.get("a"));
        if (amount < 0) return "Invalid bounds!";
        String result = checkType(options.get("i"));
        ResourceType resourceType; Weapons weapons; Food food;
        switch (result) {
            case "food" : food = Food.valueOf(options.get("i").toUpperCase().replaceAll(" ","_"));
                            if (food.getPrice() * amount < currentKingdom.getBalance()) return "You do not have enough balance!";
                            if (currentKingdom.getBuildingFormKingdom(BuildingType.FOOD_STOCKPILE) == null)
                                return "You do not have any stock to put food in it!";
                            if (currentKingdom.getNumberOfStock(BuildingType.FOOD_STOCKPILE) * StockType.FOOD_STOCKPILE.getCAPACITY() <
                                    currentKingdom.getFoodAmount(food) + amount) return "You do not have enough space for this food!";
                            break;
            case "weapon" : weapons = Weapons.valueOf(options.get("i").toUpperCase().replaceAll(" ","_"));
                            if (weapons.getCost() * amount < currentKingdom.getBalance()) return "You do not have enough balance!";
                            if (currentKingdom.getBuildingFormKingdom(BuildingType.ARMOURY) == null)
                                return "You do not have any stock to put weapon in it!";
                            if (currentKingdom.getNumberOfStock(BuildingType.ARMOURY) * StockType.ARMOURY.getCAPACITY() <
                                currentKingdom.getWeaponAmount(weapons) + amount) return "You do not have enough space for this weapon!";
                            break;
            case "resource" : resourceType = ResourceType.valueOf(options.get("i").toUpperCase().replaceAll(" ","_"));
                            if (resourceType.getPrice() * amount > currentKingdom.getBalance()) return "You do not have enough balance!";
                            if (currentKingdom.getNumberOfStock(BuildingType.STOCKPILE) * StockType.STOCKPILE.getCAPACITY() <
                                    currentKingdom.getResourceAmount(resourceType) + amount) return "You do not have enough space for this resource!";
                            break;
            default: return "Item not found in the shop!";
        }
        return "done";
    }
    public String sellFromShop(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        int amount = Integer.parseInt(options.get("a"));
        if (amount < 0) return "Invalid bounds!";
        String result = checkType(options.get("i"));
        ResourceType resourceType; Weapons weapons; Food food;
        switch (result) {
            case "food" : food = Food.valueOf(options.get("i").toUpperCase().replaceAll(" ","_"));
                if (currentKingdom.getFoodAmount(food) < amount) return "You do not have enough foods!";
            case "weapon" : weapons = Weapons.valueOf(options.get("i").toUpperCase().replaceAll(" ","_"));
                if (currentKingdom.getWeaponAmount(weapons) < amount) return "You do not have enough weapon!"; break;
            case "resource" : resourceType = ResourceType.valueOf(options.get("i").toUpperCase().replaceAll(" ","_"));
                if (currentKingdom.getResourceAmount(resourceType) < amount) return "You do not have enough resource!"; break;
            default: return "Item not found in the shop!";
        }
        return "done";
    }

    public String verified(HashMap<String, String> options, Integer sign, double rate) {
        int amount = Integer.parseInt(options.get("a"));
        String result = checkType(options.get("i"));
        ResourceType resourceType; Weapons weapons; Food food;
        switch (result) {
            case "food" : food = Food.valueOf(options.get("i").toUpperCase().replaceAll(" ","_"));
                currentKingdom.setFoodsAmount(food,sign * amount); currentKingdom.setBalance((double) -sign * food.getPrice() * amount * rate); break;
            case "weapon" : weapons = Weapons.valueOf(options.get("i").toUpperCase().replaceAll(" ","_"));
                currentKingdom.setWeaponsAmount(weapons,sign * amount); currentKingdom.setBalance((double) -sign * weapons.getCost() * amount * rate); break;
            case "resource" : resourceType = ResourceType.valueOf(options.get("i").toUpperCase().replaceAll(" ","_"));
                currentKingdom.setResourceAmount(resourceType,sign * amount); currentKingdom.setBalance((double) -sign * resourceType.getPrice() * amount * rate);
        }
        return "Purchase done successfully";
    }

    public String positionValidate(String xPosition, String yPosition) {
        if(!xPosition.matches("-?\\d+") && !yPosition.matches("-?\\d+"))
            return "Please input digit as your input values!";
        if (Integer.parseInt(xPosition) > gameMap.getMapWidth() ||
                Integer.parseInt(yPosition) > gameMap.getMapWidth() ||
                Integer.parseInt(xPosition) < 0 ||
                Integer.parseInt(yPosition) < 0) return "Invalid bounds!";
        return null;
    }

    public String moveSiege(HashMap<String , String> options) {
        if (!(selectedBuilding instanceof Siege)) return "Invalid command";
        SiegeType siegeType = (SiegeType) selectedBuilding.getSpecificConstant();
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        String position = positionValidate(options.get("x"), options.get("y"));
        if (position != null) return position;
        MapBlock goingPosition = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if (!siegeType.getPortable()) return "This siege is not movable!";
        if (!goingPosition.getMapBlockType().isAccessible()) return "You can not go to this block!";
        if (goingPosition.getBuildings() != null || goingPosition.getSiege() != null) return "This block is already filled with another building!";
        if (gameMap.getShortestWayLength(selectedBuilding.getPosition().getxPosition(), selectedBuilding.getPosition().getyPosition()
                ,goingPosition.getxPosition(), goingPosition.getyPosition(), siegeType.getMoveRange()) == null)
            return "This siege is too slow to reach such destination!";
        int counter = 0;
        while (selectedBuilding.getBuildingType().getNumberOfWorker() != counter || selectedBuilding.getPosition().getUnits().size() != 0) {
            for (Unit unit : selectedBuilding.getPosition().getUnits())
                if (unit.getUnitType().equals(UnitType.ENGINEER) && unit.getUnitState().equals(UnitState.WORKING)) {
                    unit.setLocationBlock(goingPosition);
                    currentKingdom.setRemainingUnitMove(unit);
                    selectedBuilding.getPosition().removeUnitFromHere(unit);
                    counter++;
                    break;
                }
        }
        selectedBuilding.getPosition().setSiege(null);
        selectedBuilding.setPosition(goingPosition);
        currentKingdom.setRemainingBuildingMove(selectedBuilding);
        return "Your siege moved successfully!";
    }
}
