package controller;

import model.*;
import model.building.Building;
import model.building.BuildingType;
import model.unit.Unit;
import model.unit.UnitState;
import model.unit.UnitType;
import view.*;
import java.util.HashMap;

public class GameController {
    public static Map gameMap;
    public static Building selectedBuilding;
    private final GameMenu gameMenu;
    private final UnitMenu unitMenu;
    private final User currentUser;
    private Unit currentUnit;

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

    public String positionValidate(String xPosition, String yPosition) {
        if(!xPosition.matches("-?\\d+") && !yPosition.matches("-?\\d+"))
            return "Please input digit as your input values!";
        if (Integer.parseInt(xPosition) > gameMap.getMapWidth() ||
                Integer.parseInt(yPosition) > gameMap.getMapWidth() ||
                Integer.parseInt(xPosition) < 0 ||
                Integer.parseInt(yPosition) < 0) return "Invalid bounds!";
        return null;
    }

    public String dropBuilding(HashMap<String, String> options) {
        try {BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));}
        catch (Exception ignored) {return "There is not such building in the game!";}
        String result;
        result = positionValidate(options.get("x"),options.get("y"));
        if (result != null) return result;
        MapBlock mapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if (!mapBlock.getMapBlockType().isBuildable())
            return "You can not build your building here. Please choose another location!";
        if (mapBlock.getBuildings() != null)
            return "This block already has filled with another building. Please choose another location!";
        BuildingType buildingType = BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));
        if (buildingType.equals(BuildingType.HEAD_QUARTER)) return "You can not buy this building!";
        if (buildingType.getGOLD() > gameMap.getKingdomByOwner(currentUser).getBalance())
            return "You do not have enough gold to buy this building.";
        if (buildingType.getRESOURCE_NUMBER() > gameMap.getKingdomByOwner(currentUser).getResourceAmount(buildingType.getRESOURCES()))
            return "You do not have enough " + buildingType.getRESOURCES().name().toLowerCase() + " to buy this building.";
        Building building = new Building(mapBlock, buildingType);
        gameMap.getKingdomByOwner(currentUser).setBalance((double) -buildingType.getGOLD());
        gameMap.getKingdomByOwner(currentUser).setResourceAmount(buildingType.getRESOURCES(),-buildingType.getRESOURCE_NUMBER());
        mapBlock.setBuildings(building);
        gameMap.getKingdomByOwner(currentUser).addBuilding(building);
        //TODO Farm should be check for being cultivable
        return buildingType.name().toLowerCase().replaceAll("_"," ") + " added successfully to your kingdom.";
    }

    public String selectBuilding(HashMap<String, String> options) {
        String result;
        result = positionValidate(options.get("x"),options.get("y"));
        if (result != null) return result;
        MapBlock mapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if (mapBlock.getBuildings() == null) return "There is no building found in this position!";
        if (!gameMap.getBuildingOwnerByPosition(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y"))).equals(currentUser))
            return "You can not access to this building cause you do not own it!";
        GameController.selectedBuilding = mapBlock.getBuildings();
        return "building";
    }

    public String nextTurn(){
        return null;
    }

    public String selectUnit(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        int x = Integer.parseInt(options.get("x"));
        int y = Integer.parseInt(options.get("y"));
        if (x <= gameMap.getMapWidth() && y <= gameMap.getMapHeight()) {
            UnitType unitType;
            if ((unitType = UnitType.valueOf(options.get("t").toUpperCase().replaceAll("\\s*",""))) != null){
                for (Unit unit : gameMap.getMapBlockByLocation(x, y).getUnits()) {
                    if (unit.getUnitType().equals(unitType)) {
                        if (unit.getOwner().equals(currentUser)) {
                            currentUnit = unit;
                            return "unit selected";
                        }
                    }
                }
                return "You do not have such a soldier in this block";
            } else
                return "type entered not valid";
        }
        else
            return "your location out of bounds";
    }

    public String moveUnit(HashMap<String, String > options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        int x = Integer.parseInt(options.get("x"));
        int y = Integer.parseInt(options.get("y"));
        if (x <= gameMap.getMapWidth() && y <= gameMap.getMapHeight()) {
            if (!(gameMap.getMapBlockByLocation(x, y).getMapBlockType().name().equals("WATER")) ||
            !(gameMap.getMapBlockByLocation(x, y).getMapBlockType().name().equals("MOUNTAIN")) || (gameMap.getMapBlockByLocation(x, y).getBuildings() != null)) {
                if (currentUnit.getXPosition() - x + currentUnit.getYPosition() - y <= currentUnit.getUnitType().getVELOCITY()) {
                    //TODO
                } else
                    return "The speed of the soldier is not enough";
            } else
                return "The soldier can go to that location";
        }
        else
            return "your location out of bounds";
        return null;
    }

    public String setSituation(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        int x = Integer.parseInt(options.get("x"));
        int y = Integer.parseInt(options.get("y"));
        if (x <= gameMap.getMapWidth() && y <= gameMap.getMapHeight()) {
            UnitType unitType;
            if ((unitType = UnitType.valueOf(options.get("t").toUpperCase().replaceAll("\\s*",""))) != null){
                for (Unit unit : gameMap.getMapBlockByLocation(x, y).getUnits()) {
                    if (unit.getUnitType().equals(unitType)) {
                        if (unit.getOwner().equals(currentUser)) {
                            UnitState unitState = UnitState.valueOf(options.get("s").toUpperCase());
                            unit.setUnitState(unitState);
                            return "The state is set correctly";
                        }
                    }
                }
                return "You do not have such a soldier in this block";
            } else
                return "type entered not valid";
        }
        else
            return "your location out of bounds";
    }

    public String attackOnUnit(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        int x = Integer.parseInt(options.get("x"));
        int y = Integer.parseInt(options.get("y"));
        if (x <= gameMap.getMapWidth() && y <= gameMap.getMapHeight()) {
            UnitType unitType;
            if ((unitType = UnitType.valueOf(options.get("t").toUpperCase().replaceAll("\\s*",""))) != null){
                for (Unit unit : gameMap.getMapBlockByLocation(x, y).getUnits()) {
                    if (unit.getUnitType().equals(unitType)) {
                        if (!(unit.getOwner().equals(currentUser))) {
                            currentUnit.setForAttack(unit);
                            return "attacked";
                        }
                    }
                }
                return "do not exist such a soldier in this block";
            } else
                return "type entered not valid";
        }
        else
            return "your location out of bounds";

    } //TODO این سه تا دستور یونیت شباهت خیلی زیادی دارن. میشه یه تابع برا اروراش زد

    public String setTaxRate(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        int taxRate = Integer.parseInt(options.get("r"));
        if (taxRate >= -3 && taxRate <= 8) {
            Kingdom kingdom = gameMap.getKingdomByOwner(currentUser);
            Double moneyForPay = 2 - (8 - taxRate) * 0.2;
            if (kingdom.getPopularity() * moneyForPay <= kingdom.getBalance()) {
                kingdom.setTaxRate(taxRate);
                return "tax rate settled";
                //در نکست ترن باید از حسابش به اندازه ای که دیگه هست کم بشه. اگه پول نداشت اونجا باید ریت رو بر روی صفر تنظیم کرد
            } else
                return "your balance is not enough";
        } else
            return "tax rate out of bounds";
    }

    public String showTaxRate(HashMap<String, String> options) {
        return "tax rate: " + gameMap.getKingdomByOwner(currentUser).getTaxRate();
    }

}