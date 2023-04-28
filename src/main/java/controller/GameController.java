package controller;

import model.*;
import model.building.Building;
import model.building.BuildingType;
import view.*;

import java.util.HashMap;

public class GameController {
    public static Map gameMap;
    private final GameMenu gameMenu;
    private final UnitMenu unitMenu;
    private final User currentUser;

    public GameController(Map gameMap) {
        GameController.gameMap = gameMap;
        this.gameMenu = new GameMenu(this);
        this.unitMenu = new UnitMenu(this);
        currentUser = Controller.currentUser;
    }

    public void run(){
        ShopAndTradeController shopAndTradeController = new ShopAndTradeController();
        System.out.println("Welcome to game menu:))");
        while (true) {
            switch (gameMenu.run()) {
                case "map":
                    break;
                case "trade":
                    shopAndTradeController.runTrade();
                    break;
                case "shop":
                    shopAndTradeController.runShop();
                    break;
                case "building":
                    BuildingController buildingController = new BuildingController();
                    buildingController.run();
                    break;
                case "unit":
                    unitMenu.run();
                    break;
                case "back": return;
            }
        }
    }

    public String dropBuilding(HashMap<String, String> options) {
        try {BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));}
        catch (Exception ignored) {return "There is not such building in the game!";}
        if(!options.get("x").matches("-?\\d+") && !options.get("x").matches("-?\\d+"))
            return "Please input digit as your input values!";
        if (Integer.parseInt(options.get("x")) > gameMap.getMapWidth() ||
                Integer.parseInt(options.get("y")) > gameMap.getMapWidth() ||
                Integer.parseInt(options.get("x")) < 0 ||
                Integer.parseInt(options.get("y")) < 0) return "Invalid bounds!";
        MapBlock mapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if (!mapBlock.getMapBlockType().isBuildable())
            return "You can not build your building here. Please choose another location!";
        if (mapBlock.getBuildings() != null || mapBlock.getUnits().size() != 0)
            return "This block already has filled with another building or some sort of units. Please choose another location!";
        BuildingType buildingType = BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));
        if (buildingType.getGOLD() > gameMap.getKingdomByOwner(currentUser).getBalance())
            return "You do not have enough gold to buy this building.";
        if (buildingType.equals(BuildingType.HEAD_QUARTER)) return "You can not buy this building!";
        if (buildingType.getRESOURCE_NUMBER() > gameMap.getKingdomByOwner(currentUser).getResourceAmount(buildingType.getRESOURCES()))
            return "You do not have enough " + buildingType.getRESOURCES().name().toLowerCase() + " to buy this building.";
        Building building = new Building(mapBlock, buildingType);
        mapBlock.setBuildings(building);
        gameMap.getKingdomByOwner(currentUser).addBuilding(building);
        //TODO Farm should be check for being cultivable
        return buildingType.name().toLowerCase().replaceAll("_"," ") + " added successfully to your kingdom.";
    }

    public String nextTurn(){
        return null;
    }

}
