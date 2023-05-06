package controller;

import model.*;
import model.building.*;
import model.unit.Unit;
import model.unit.UnitState;
import model.unit.UnitType;
import view.*;
import java.util.HashMap;


public class GameController {
    public static Map gameMap;
    public static Building selectedBuilding;
    private final GameMenu gameMenu;
    private User currentUser;
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

    public void run() {
        System.out.println("Welcome to game menu:))");
        while (true) {
            switch (gameMenu.run()) {
                case "map":
                    MapController mapController = new MapController(gameMap, currentUser, XofMap, YofMap);
                    mapController.run();
                    break;
                case "trade":
                    ShopAndTradeController tradeController = new ShopAndTradeController();
                    tradeController.runTrade();
                    break;
                case "shop":
                    ShopAndTradeController shopController = new ShopAndTradeController();
                    shopController.runShop();
                    break;
                case "building":
                    BuildingController buildingController = new BuildingController();
                    buildingController.run();
                    break;
                case "unit":
                    UnitController unitController = new UnitController(gameMap, currentKingdom, selectedUnit);
                    break;
                case "next turn":
                    Turn turn = new Turn(); turn.runNextTurn();
                    break;
                case "back": return;
            }
        }
    }

    public String nextTurn(){
        int nextPerson = gameMap.getPlayers().indexOf(currentKingdom);
        Controller.currentUser = gameMap.getPlayers().get((nextPerson + 1) % gameMap.getPlayers().size()).getOwner();
        currentUser = Controller.currentUser;
        selectedUnit = null; selectedBuilding = null;
        currentKingdom = gameMap.getKingdomByOwner(currentUser);
        return "next turn";
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
        for (String key : options.keySet())
            if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        try {BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));}
        catch (Exception ignored) {return "There is no such a building!";}
        if (BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_")).specificConstant instanceof SiegeType)
            return "There is no such a building!";
        String result;
        result = positionValidate(options.get("x"),options.get("y"));
        if (result != null) return result;
        MapBlock mapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if (!currentKingdom.checkOutOfRange(mapBlock.getxPosition(), mapBlock.getyPosition()))
            return "This block is out of range of your kingdom!";
        if (!mapBlock.getMapBlockType().isBuildable())
            return "You can not build your building here. Please choose another location!";
        if (mapBlock.getBuildings() != null || mapBlock.getSiege() != null)
            return "This block already has filled with another building. Please choose another location!";
        BuildingType buildingType = BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));
        if (buildingType.equals(BuildingType.HEAD_QUARTER)) return "You can not buy this building!";
        if (buildingType.getGOLD() > currentKingdom.getBalance())
            return "You do not have enough gold to buy this building.";
        if (buildingType.getRESOURCES() != null &&
                buildingType.getRESOURCE_NUMBER() > gameMap.getKingdomByOwner(currentUser).getResourceAmount(buildingType.getRESOURCES()))
            return "You do not have enough " + buildingType.getRESOURCES().name().toLowerCase() + " to buy this building.";
        Building building;
        if (buildingType.specificConstant == null) building = new Building(mapBlock, buildingType, currentKingdom);
        else if (buildingType.specificConstant instanceof DefensiveStructureType) building = new DefensiveStructure(mapBlock, buildingType, currentKingdom);
        else if (buildingType.specificConstant instanceof CampType) building = new Camp(mapBlock, buildingType, currentKingdom);
        else if (buildingType.specificConstant instanceof StockType) building = new Stock(mapBlock, buildingType, currentKingdom);
        else if(buildingType.specificConstant instanceof ProducerType) building = new Producer(mapBlock, buildingType, currentKingdom);
        else building = new GeneralBuilding(mapBlock, buildingType, currentKingdom);
        currentKingdom.setBalance((double) -buildingType.getGOLD());
        currentKingdom.setResourceAmount(buildingType.getRESOURCES(),-buildingType.getRESOURCE_NUMBER());
        mapBlock.setBuildings(building);
        currentKingdom.addBuilding(building);
        //TODO Farm should be check for being cultivable
        return buildingType.name().toLowerCase().replaceAll("_"," ") + " added successfully to kingdom.";
    }

    public MapBlock findTheNearestFreeBlock(int x, int y) {
        int counter = 1;
        while (true) {
            for (int i = -counter; i < counter; i++) {
                if ( i + x  > gameMap.getMapWidth() || i + x < 0) continue;
                for (int j = -counter; j < counter; j++) {
                    if ( j + y  > gameMap.getMapHeight() || j + y < 0) continue;
                    if (gameMap.getMapBlockByLocation(x + i, y + j).getBuildings() == null &&
                            gameMap.getMapBlockByLocation(x + i, y + j).getSiege() == null &&
                            gameMap.getMapBlockByLocation(x + i, y + j).getMapBlockType().isBuildable())
                        return gameMap.getMapBlockByLocation(x + i, y + j);
                }
            }
            counter++;
            if (counter > gameMap.getMapHeight() && counter > gameMap.getMapWidth()) return null;
        }
    }
    public String dropSiege(HashMap<String, String> options) {
        for (String key : options.keySet())
            if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        try {BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));}
        catch (Exception ignored) {return "There is no such a siege!";}
        boolean check  = BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_")).specificConstant instanceof SiegeType;
        if (!check) return "There is no such a siege!";
        String result;
        result = positionValidate(options.get("x"),options.get("y"));
        if (result != null) return result;
        BuildingType buildingType = BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));
        SiegeType siegeType = (SiegeType) BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_")).specificConstant;
        MapBlock mapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if (siegeType.getPortable()) {
            if (!mapBlock.getMapBlockType().isBuildable()) return "You can not build your sieges here!";
            if (mapBlock.getBuildings() != null || mapBlock.getSiege() != null) return "This block already filed with buildings please make your siege somewhere else!";
        }
        else {
            if (mapBlock.getBuildings() == null) return "There is no building found in this block to put siege on it!";
            if (!mapBlock.getBuildings().getOwner().getOwner().equals(currentUser))
                return "You can not build siege on the building you do not own!";
            if (!(mapBlock.getBuildings().getBuildingType().specificConstant instanceof DefensiveStructureType))
                return "You should put this siege on part of castles!";
            DefensiveStructureType defensiveStructureType = (DefensiveStructureType) mapBlock.getBuildings().getBuildingType().specificConstant;
            if (!defensiveStructureType.getHoldSiege()) return "This part of castle can not hold sieges!";
            if (mapBlock.getSiege() != null) return "This building already has field with another building!";
        }
        if (currentKingdom.getBalance() < buildingType.getGOLD()) return "You do not have enough gold to buy this building.";
        if (currentKingdom.getEngineer() < siegeType.getEngineerNeeded()) return "There is not enough engineer to build this siege!";
        if (findTheNearestFreeBlock(mapBlock.getxPosition(), mapBlock.getyPosition()) == null)
            return "There is no available block on map to put siege tent!";
        currentKingdom.setBalance((double) -buildingType.getGOLD());
        Building building = new Building(mapBlock ,buildingType, currentKingdom);
        Building tent = new Building(findTheNearestFreeBlock(mapBlock.getxPosition(), mapBlock.getyPosition()), BuildingType.SIEGE_TENT, currentKingdom);
        currentKingdom.addBuilding(tent);
        currentKingdom.addBuilding(building);
        findTheNearestFreeBlock(mapBlock.getxPosition(), mapBlock.getyPosition()).setBuildings(tent);
        mapBlock.setSiege(building);
        return buildingType.name().toLowerCase().replaceAll("_"," ") + " added successfully to your kingdom.";
    }

    public String selectBuilding(HashMap<String, String> options) {
        for (String key : options.keySet())
            if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        String result;
        result = positionValidate(options.get("x"),options.get("y"));
        if (result != null) return result;
        MapBlock mapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if (mapBlock.getBuildings() == null) return "There is no building found in this position!";
        if (!gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")))
                .getBuildings().getOwner().getOwner().equals(currentUser))
            return "You can not access to this building cause you do not own it!";
        GameController.selectedBuilding = mapBlock.getBuildings();
        return "building";
    }

    public String moveSiege(HashMap<String , String> options) {
        for (String key : options.keySet())
            if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        String firstResult, secondResult;
        firstResult = positionValidate(options.get("x"),options.get("y"));
        if (firstResult != null) return firstResult;
        MapBlock firstMapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        secondResult = positionValidate(options.get("x"),options.get("y"));
        if (secondResult != null) return secondResult;
        MapBlock secondMapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if (firstMapBlock.getSiege() == null ) return "There is no siege to be moved in this block!";
        if (firstMapBlock.getSiege() != null && firstMapBlock.getBuildings() != null) return "This siege is not movable!";
        if (secondMapBlock.getMapBlockType() != null || secondMapBlock.getSiege() != null)
            return "The block you choose to move to is already filled!";
        if (!secondMapBlock.getMapBlockType().isBuildable()) return "You can not put your siege here!";
        secondMapBlock.setSiege(firstMapBlock.getSiege());
        firstMapBlock.setSiege(null);
        secondMapBlock.getSiege().setPosition(secondMapBlock);
        return "Your siege move successfully!";
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

    public String moveUnit(HashMap<String, String > options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        int x = Integer.parseInt(options.get("x"));
        int y = Integer.parseInt(options.get("y"));
        if (x <= gameMap.getMapWidth() && y <= gameMap.getMapHeight()) {
            if (!(gameMap.getMapBlockByLocation(x, y).getMapBlockType().name().equals("WATER")) ||
            !(gameMap.getMapBlockByLocation(x, y).getMapBlockType().name().equals("MOUNTAIN")) || (gameMap.getMapBlockByLocation(x, y).getBuildings() != null)) {
                if (selectedUnit.getXPosition() - x + selectedUnit.getYPosition() - y <= selectedUnit.getUnitType().getVELOCITY()) {
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
                            selectedUnit.setForAttack(unit);
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
            Double moneyForPay = 2 - (8 - taxRate) * 0.2;
            if (currentKingdom.getPopulation() * moneyForPay <= currentKingdom.getBalance()) {
                currentKingdom.setTaxRate(taxRate);
                return "tax rate settled";
                //در نکست ترن باید از حسابش به اندازه ای که دیگه هست کم بشه. اگه پول نداشت اونجا باید ریت رو بر روی صفر تنظیم کرد
            } else
                return "your balance is not enough";
        } else
            return "tax rate out of bounds";
    }

    public String showTaxRate() {
        return "tax rate: " + currentKingdom.getTaxRate();
    }

    public String showPopularityFactors() {
        return "-food\n-tax rate\n-religion\n-fear rate";

    }

    public String showPopularity() {
        return "your popularity : " + currentKingdom.getPopularity();
    }

    public String showFoodList() {
        String output = "food list:";
        for (Food food : currentKingdom.getFoods().keySet()) {
            output += "\n" + food.name().toLowerCase() + ": " + currentKingdom.getFoods().get(food);
        }
        return output;
    }

    public String setFoodRate(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (options.get("n").matches("(\\-)?\\d+")) {
            int rateNumber = Integer.parseInt(options.get("n"));
            if (rateNumber <= 2 && rateNumber >= -2) {
                currentKingdom.setFoodRate(rateNumber);
                return "food rate settled";
            } else
                return "rate number out of bounds";
        } else
            return "rate number is not valid";
    }

    public String showFoodRate() {
        return "food rate: " + currentKingdom.getFoodRate();
    }

    public String setFearRate(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (options.get("n").matches("(\\-)?\\d+")) {
            int rateNumber = Integer.parseInt(options.get("n"));
            if (rateNumber <= 5 && rateNumber >= -5) {
                currentKingdom.setFearRate(rateNumber);
                return "fear rate settled";
            } else
                return "rate number out of bounds";
        } else
            return "rate number is not valid";
    }

    public void updateTaxRateByKingdom(Kingdom kingdom) {
        Double cost = getCostByTaxRate(kingdom.getTaxRate()) * kingdom.getPopulation();
        if ((kingdom.getTaxRate() < 0) && (cost >= kingdom.getBalance())) {
            kingdom.setTaxRate(0);
            System.out.println("your tax rate set to zero");
        }else {
            kingdom.setBalance(kingdom.getBalance() - cost);
            kingdom.setPopularity(kingdom.getPopularity() + getPopularityByTaxRate(kingdom.getTaxRate()));
        }
    }

    public void updateFearRateByKingdom(Kingdom kingdom) {
        int x = kingdom.getHeadquarter().getPosition().getxPosition();
        int y = kingdom.getHeadquarter().getPosition().getyPosition();
        for (MapBlock[] mapBlocks : gameMap.getSurroundingArea(x, y, 1)) {
            for (MapBlock block: mapBlocks) {
                if (block.getBuildings().getBuildingType().equals(BuildingType.BALLISTA))
                    if (kingdom.getMaxFearRate() > 0)
                         kingdom.setMaxFearRate(kingdom.getMaxFearRate() - 1);
            }
        }
        if (kingdom.getFearRate() > kingdom.getMaxFearRate())
            kingdom.setFearRate(kingdom.getMaxFearRate());
        kingdom.setEfficiency(100 - 10 * kingdom.getFearRate());
        kingdom.setPopularity(kingdom.getPopularity() - 2 * (kingdom.getFearRate()));
    }

    /*public void updateFoodRate(Kingdom kingdom) {
        int balanceFood = 0;
        int diversity = 0;
        double reduceFood = (1 + (0.5 * kingdom.getFoodRate())) * kingdom.getPopulation();
        for (Double foodValue : kingdom.getFoods().values()) {
            if (foodValue > 0)
                diversity++;
            balanceFood += foodValue;
        }
        if (balanceFood < reduceFood)
            kingdom.setFoodRate(-2);
        if (kingdom.getFoodRate() != -2) {
            kingdom.setPopularity(kingdom.getPopularity() + diversity + (4 * kingdom.getFoodRate()));
            for (Double foodValue : kingdom.getFoods().values()) {
                if (foodValue < reduceFood) {
                    reduceFood -= foodValue;
                    foodValue = 0.0;
                    continue;
                }
                foodValue -= reduceFood;
                break;
            }
        }
    }*/

    public void updateReligionByKingdom(Kingdom kingdom) {
        for (Building building : kingdom.getBuildings()) {
            if (building.getBuildingType().name().equals("CHURCH"))
                kingdom.setPopularity(kingdom.getPopularity() + 3);
            else if (building.getBuildingType().name().equals("CATHEDRAL"))
                kingdom.setPopularity(kingdom.getPopularity() + 5);
        }
    }

    public void updatePopulation(Kingdom kingdom) {
        kingdom.setPopulation(kingdom.getPopulation() + kingdom.getPopularity());
    }

    public Integer getPopularityByTaxRate(int taxRate) {
        if (taxRate == -3)
            return 7;
        else if (taxRate == -2)
            return 5;
        else if (taxRate == -1)
            return 3;
        else if (taxRate == 0)
            return 1;
        else if (taxRate == 1)
            return -2;
        else if (taxRate == 2)
            return -4;
        else if (taxRate == 3)
            return -6;
        else if (taxRate == 4)
            return -8;
        else if (taxRate == 5)
            return -12;
        else if (taxRate == 6)
            return -16;
        else if (taxRate == 7)
            return -20;
        else
            return -24;
    }

    public Double getCostByTaxRate(int taxRate) {
        if (taxRate == -3)
            return 1.0;
        else if (taxRate == -2)
            return 0.8;
        else if (taxRate == -1)
            return 0.6;
        else if (taxRate == 0)
            return 0.0;
        else if (taxRate == 1)
            return -0.6;
        else if (taxRate == 2)
            return -0.8;
        else if (taxRate == 3)
            return -1.0;
        else if (taxRate == 4)
            return -1.2;
        else if (taxRate == 5)
            return -1.4;
        else if (taxRate == 6)
            return -1.6;
        else if (taxRate == 7)
            return -1.8;
        else
            return -2.0;
    }


}