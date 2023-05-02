package controller;

import model.Kingdom;
import model.Map;
import model.User;
import model.building.*;
import model.unit.Unit;
import model.unit.UnitType;
import view.BuildingMenu;
import java.util.HashMap;
import java.util.Objects;

public class BuildingController {
    private final BuildingMenu buildingMenu;
    private Building selectedBuilding;
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
        else return null;
    }

    public String buildingName() {
        return "Building Name : " + selectedBuilding.getBuildingType().name().toLowerCase().replaceAll("_", " ");
    }

    public String buildingHp() {
        return "Hp : " + selectedBuilding.getHp() + " / " + selectedBuilding.getBuildingType().getHP_IN_FIRST();
    }

    public String repairBuilding() {
        //TODO ENGEENEAR
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
                        return "This block is near to your building block and it should be free of soldier enemies while building is being repaid.";
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
        //TODO BUILDING WEAPON AND STOCK ...
        if (unitType.getWEAPON_NEEDED() != null)
            if (currentKingdom.getResourceAmount(unitType.getWEAPON_NEEDED().getResourceType()) < unitType.getWEAPON_NEEDED().getResourceAmount() * count)
                return "You do not have enough resources for units equipment!";
        if (unitType.getArmour_Needed() != null)
            if (currentKingdom.getResourceAmount(unitType.getArmour_Needed().getResourceType()) < unitType.getArmour_Needed().getResourceAmount() * count)
                return "You do not have enough resources for units equipment!";
        Building building = selectedBuilding;
        Camp camp = (Camp) building;
        if (campType.getCapacity() < camp.getCapacity()) return "Your camp is full. Please make a new camp!";
        if (currentKingdom.getNoneEmployed() < count) return "You do not have enough population to make new units!";
        Unit unit = new Unit(unitType, selectedBuilding.getPosition(), currentKingdom);
        for (int i = 0; i < count ;i++) {camp.setUnits(unit); selectedBuilding.getPosition().setUnits(unit);}
        currentKingdom.addUnit(unit);
        camp.setCapacity(count);
        currentKingdom.setBalance((double) -unitType.getPRICE() * count );
        currentKingdom.setNoneEmployed(-count);
        if (unitType.getWEAPON_NEEDED() != null)
            currentKingdom.setResourceAmount(unitType.getWEAPON_NEEDED().getResourceType(), -unitType.getWEAPON_NEEDED().getResourceAmount() * count);
        if (unitType.getArmour_Needed() != null)
            currentKingdom.setResourceAmount(unitType.getArmour_Needed().getResourceType(), -unitType.getArmour_Needed().getResourceAmount() * count);
        return count + " " + unitType.name().toLowerCase().replaceAll("_"," ") + " has been made!";
    }
}
