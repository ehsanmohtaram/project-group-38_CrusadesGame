package controller;

import model.*;
import model.building.Building;
import model.building.BuildingType;
import model.building.SiegeType;
import model.unit.Unit;
import model.unit.UnitState;
import model.unit.UnitType;
import view.*;
import java.util.HashMap;

import java.util.HashMap;

public class GameController {
    public static Map gameMap;
    public static Building selectedBuilding;
    private final GameMenu gameMenu;
    private final User currentUser;
    private Unit selectedUnit;
    private Kingdom currentKingdom;
    private int XofMap;
    private int YofMap;

    public GameController(Map gameMap) {
        GameController.gameMap = gameMap;
        this.gameMenu = new GameMenu(this);
        currentUser = Controller.currentUser;
        currentKingdom = gameMap.getKingdomByOwner(currentUser);
    }

    public void run(){
        ShopAndTradeController shopAndTradeController = new ShopAndTradeController();
        System.out.println("Welcome to game menu:))");
        while (true) {
            switch (gameMenu.run()) {
                case "map":
                    MapController mapController = new MapController(gameMap, currentUser, XofMap, YofMap);
                    mapController.run();
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
                    UnitController unitController = new UnitController(gameMap, currentKingdom, selectedUnit);
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
        catch (Exception ignored) {return "There is no such a building!";}
        if (BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_")).specificConstant instanceof SiegeType)
            return "There is no such a building!";
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
        Building building = new Building(mapBlock, buildingType, currentKingdom);
        gameMap.getKingdomByOwner(currentUser).setBalance((double) -buildingType.getGOLD());
        gameMap.getKingdomByOwner(currentUser).setResourceAmount(buildingType.getRESOURCES(),-buildingType.getRESOURCE_NUMBER());
        mapBlock.setBuildings(building);
        gameMap.getKingdomByOwner(currentUser).addBuilding(building);
        //TODO Farm should be check for being cultivable
        return buildingType.name().toLowerCase().replaceAll("_"," ") + " added successfully to your kingdom.";
    }

    /*public String dropSiege(HashMap<String, String> options) {
        try {BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));}
        catch (Exception ignored) {return "There is no such a siege!";}
        boolean check  = BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_")).specificConstant instanceof SiegeType;
        if (!check)

    }*/

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

    public String showMap(HashMap<String, String> options){
        for (String key: options.keySet())
            if(options.get(key) == null)
                return "input necessary options";
        for (String key: options.keySet())
            if(!options.get(key).matches("\\d*") )
            return "input numbers as arguments";
        int xPosition = Integer.parseInt(options.get("x")) ;
        int yPosition = Integer.parseInt(options.get("y")) ;
        XofMap = xPosition;
        YofMap = yPosition;
        return gameMap.getPartOfMap(xPosition, yPosition);
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
                            selectedUnit = unit;
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

    public String setTaxRate(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        int taxRate = Integer.parseInt(options.get("r"));
        if (taxRate >= -3 && taxRate <= 8) {
            Kingdom kingdom = gameMap.getKingdomByOwner(currentUser);
            Double moneyForPay = 2 - (8 - taxRate) * 0.2;
            if (kingdom.getPopulation() * moneyForPay <= kingdom.getBalance()) {
                kingdom.setTaxRate(taxRate);
                return "tax rate settled";
                //در نکست ترن باید از حسابش به اندازه ای که دیگه هست کم بشه. اگه پول نداشت اونجا باید ریت رو بر روی صفر تنظیم کرد
            } else
                return "your balance is not enough";
        } else
            return "tax rate out of bounds";
    }

    public String showTaxRate() {
        return "tax rate: " + gameMap.getKingdomByOwner(currentUser).getTaxRate();
    }

    public String showPopularityFactors() {
        return "-food\n-tax rate\n-religion\n-fear rate";

    }

    public String showPopularity() {
        return "your popularity: " +gameMap.getKingdomByOwner(currentUser).getPopularity();
    }

    public String showFoodList() {
        Kingdom kingdom = gameMap.getKingdomByOwner(currentUser);
        String output = "food list:";
        for (Food food : kingdom.getFoods().keySet()) {
            output += "\n" + food.name().toLowerCase() + ": " + kingdom.getFoods().get(food);
        }
        return output;
    }

    public String setFoodRate(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (options.get("n").matches("(\\-)?\\d+")) {
            int rateNumber = Integer.parseInt(options.get("n"));
            if (rateNumber <= 2 && rateNumber >= -2) {
                gameMap.getKingdomByOwner(currentUser).setFoodRate(rateNumber);
                return "food rate settled";
            } else
                return "rate number out of bounds";
        } else
            return "rate number is not valid";
    }

    public String showFoodRate() {
        return "food rate: " + gameMap.getKingdomByOwner(currentUser).getFoodRate();
    }

    public String setFearRate(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (options.get("n").matches("(\\-)?\\d+")) {
            int rateNumber = Integer.parseInt(options.get("n"));
            if (rateNumber <= 5 && rateNumber >= -5) {
                gameMap.getKingdomByOwner(currentUser).setFearRate(rateNumber);
                return "fear rate settled";
            } else
                return "rate number out of bounds";
        } else
            return "rate number is not valid";
    }

}