package controller;

import model.Map;
import model.User;
import model.building.*;
import model.unit.Unit;
import view.BuildingMenu;

public class BuildingController {
    private final BuildingMenu buildingMenu;
    private final Building selectedBuilding;
    private final Map gameMap;
    private final User currentUser;
    public BuildingController() {
        buildingMenu = new BuildingMenu(this);
        selectedBuilding = GameController.selectedBuilding;
        gameMap = GameController.gameMap;
        currentUser = Controller.currentUser;
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
        if (buildingType.getRESOURCE_NUMBER() > gameMap.getKingdomByOwner(currentUser).getResourceAmount(buildingType.getRESOURCES()))
            return "You do not have enough " + buildingType.getRESOURCES().name().toLowerCase() + " to buy this building.";
        for (Unit unit : selectedBuilding.getPosition().getUnits())
            if (!unit.getOwner().equals(gameMap.getKingdomByOwner(currentUser)))
                return "This block should be free of soldier enemies while building is being repaid.";
        int xPosition = selectedBuilding.getPosition().getxPosition();
        int yPosition = selectedBuilding.getPosition().getxPosition();
        for (int i = -2; i <= 2; i++)
            for (int j = -2; j <= 2; j++)
                for (Unit unit : gameMap.getMapBlockByLocation(xPosition + i, yPosition + j).getUnits())
                    if (!unit.getOwner().equals(gameMap.getKingdomByOwner(currentUser)))
                        return "This block is near to your building block and it should be free of soldier enemies while building is being repaid.";
        if (selectedBuilding.getHp().equals(buildingType.getHP_IN_FIRST())) return "Building hp is full.";
        selectedBuilding.damage(selectedBuilding.getHp() - selectedBuilding.getBuildingType().getHP_IN_FIRST());
        return "Building repaired successfully!";
    }
}
