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
    private final Building selectedBuilding;
    private final Map gameMap;
    private final User currentUser;
    private final Kingdom currentKingdom;
    public BuildingController() {
        buildingMenu = new BuildingMenu(this);
        selectedBuilding = GameController.selectedBuilding;
        gameMap = GameController.gameMap;
        currentUser = Controller.currentUser;
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
        //TODO ENGINEER
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
        for (String key : options.keySet())
            if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        try {UnitType.valueOf(options.get("t").toUpperCase().replaceAll(" ", "_"));}
        catch (Exception ignored) {System.out.println("There is no such a unit found!");}
        if (!options.get("c").matches("-?\\d+")) return "Please input a digit as count value!";
        if (Integer.parseInt(options.get("c")) < 0 || Integer.parseInt(options.get("c")) > 20 ) return "Invalid bounds!";
        UnitType unitType = UnitType.valueOf(options.get("t").toUpperCase().replaceAll(" ", "_"));
        CampType campType = (CampType) selectedBuilding.getBuildingType().specificConstant;
        if (!Objects.equals(campType.getArab(), unitType.getIS_ARAB())) return "You can not build this type of unit here!";
        //if (unitType.getPRICE() +  > currentKingdom.getBalance()) return "You do not have enough balance to buy this unit!";
        //if (campType.getCapacity() < )
            return null;

    }
}
