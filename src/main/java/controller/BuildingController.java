package controller;

import model.*;
import model.building.*;
import model.unit.Unit;
import model.unit.UnitType;
import view.BuildingMenu;
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
        User currentUser = Controller.currentUser;
        currentKingdom = gameMap.getKingdomByOwner(currentUser);
    }

    public void run() {
        buildingMenu.mainBuildingClassRun();
    }

    public String redirect() {
        if (selectedBuilding.getSpecificConstant() instanceof DefensiveStructureType) return buildingMenu.defensiveBuildingRnu();
        else if (selectedBuilding.getSpecificConstant() instanceof CampType) return buildingMenu.campBuildingRnu();
        else if (selectedBuilding.getSpecificConstant() instanceof GeneralBuildingType) return buildingMenu.generalBuildingRun();
        else if (selectedBuilding.getSpecificConstant() instanceof StockType) return buildingMenu.stockBuildingRun();
        else if (selectedBuilding.getSpecificConstant() instanceof ProducerType) return buildingMenu.produceBuildingRun();
        else return null;
    }

    public String buildingName() {
        return "Building Name : " + selectedBuilding.getBuildingType().name().toLowerCase().replaceAll("_", " ");
    }

    public String buildingHp() {
        return "Hp : " + selectedBuilding.getHp() + " / " + selectedBuilding.getBuildingType().getHP_IN_FIRST();
    }

    public String repairBuilding() {
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

    public String createUnit(HashMap<String, String> options) {
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
        if (unitType.getPRICE() * count > currentKingdom.getBalance()) return "You do not have enough balance to buy this unit!";
        if (unitType.getWEAPON_NEEDED() != null)
            if (currentKingdom.getWeaponAmount(unitType.getWEAPON_NEEDED()) < count) return "You do not have enough resources for units equipment!";
        if (unitType.getArmour_Needed() != null)
            if (currentKingdom.getWeaponAmount(unitType.getArmour_Needed()) < count) return "You do not have enough resources for units equipment!";
        Camp camp = (Camp) selectedBuilding;
        if (campType.getCapacity() < camp.getCapacity()) return "Your camp is full. Please make a new camp!";
        if (currentKingdom.getNoneEmployed() < count) return "You do not have enough population to make new units!";
        Unit unit = new Unit(unitType, selectedBuilding.getPosition(), currentKingdom);
        for (int i = 0; i < count ;i++) {camp.setUnits(unit); selectedBuilding.getPosition().setUnits(unit);}
        currentKingdom.addUnit(unit);
        camp.setCapacity(count);
        currentKingdom.setBalance((double) -unitType.getPRICE() * count );
        currentKingdom.setNoneEmployed(-count);
        if (unitType.getWEAPON_NEEDED() != null) currentKingdom.setWeaponsAmount(unitType.getWEAPON_NEEDED(), -count);
        if (unitType.getArmour_Needed() != null) currentKingdom.setWeaponsAmount(unitType.getArmour_Needed(), -count);
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
        for (Enum<?> showResources : stock.getResourceValues().keySet()) {
            stringBuilder.append(showResources.name().toLowerCase().replaceAll("_", " ")).append(" : ")
                    .append(stock.getResourceValues().get(showResources)).append("\n");
        }
        return stringBuilder.deleteCharAt(stringBuilder.length() - 1).toString();
    }
}
