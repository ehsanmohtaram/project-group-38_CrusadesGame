package controller;

import model.*;
import model.building.*;
import model.unit.Unit;
import model.unit.UnitState;
import model.unit.UnitType;
import view.*;

import java.util.ArrayList;
import java.util.HashMap;


public class GameController {
    public static Map gameMap;
    public static Building selectedBuilding;
    public static ArrayList<Unit> selectedUnit;
    private final GameMenu gameMenu;
    private User currentUser;
    private Kingdom currentKingdom;
    private int XofMap;
    private int YofMap;

    public GameController(Map gameMap) {
        GameController.gameMap = gameMap;
        this.gameMenu = new GameMenu(this);
        currentUser = Controller.currentUser;
        currentKingdom = gameMap.getKingdomByOwner(currentUser);
        selectedUnit = new ArrayList<>();
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
                    TradeController tradeController = new TradeController();
                    tradeController.runTrade();
                    break;
                case "building":
                    BuildingController buildingController = new BuildingController();
                    buildingController.run();
                    break;
                case "unit":
                    UnitController unitController = new UnitController();
                    unitController.run();
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

    public String checkSpecificBuilding(MapBlock mapBlock, BuildingType buildingType) {
        ProducerType producerType;
        if (buildingType.specificConstant instanceof ProducerType) {
            producerType = (ProducerType) buildingType.specificConstant;
            if (producerType.getFarm() && !mapBlock.getMapBlockType().isCultivable()) return "This block is not appropriate for farms!";
        }
        MineType mineType;
        if (buildingType.specificConstant instanceof MineType) {
            mineType = (MineType) buildingType.specificConstant;
            if (!mineType.getMapBlockType().equals(mapBlock.getMapBlockType()))
                return "You should build this building on its resources!";
        }
        return null;
    }

    public void changeNonWorkingUnitPosition(UnitType unitType, MapBlock mapBlock, int number) {
        Camp camp;
        int counter = 0;
        for (Unit unit : currentKingdom.getUnits()) {
            if (counter == number) return;
            if (unit.getUnitType().equals(unitType) && unit.getUnitState().equals(UnitState.NOT_WORKING)) {
                if (unit.getLocationBlock().getBuildings() instanceof Camp) {
                    camp = (Camp) unit.getLocationBlock().getBuildings();
                    camp.setCapacity(-1);
                }
                mapBlock.setUnits(unit);
                unit.getLocationBlock().getUnits().remove(unit);
                unit.setLocationBlock(mapBlock);
                unit.setUnitState(UnitState.WORKING);
                counter++;
            }
        }
    }

    public void createBuilding(MapBlock mapBlock, BuildingType buildingType) {
        Building building;
        if (buildingType.specificConstant == null) building = new Building(mapBlock, buildingType, currentKingdom);
        else if (buildingType.specificConstant instanceof DefensiveStructureType) building = new DefensiveStructure(mapBlock, buildingType, currentKingdom);
        else if (buildingType.specificConstant instanceof CampType) building = new Camp(mapBlock, buildingType, currentKingdom);
        else if (buildingType.specificConstant instanceof StockType) building = new Stock(mapBlock, buildingType, currentKingdom);
        else if(buildingType.specificConstant instanceof ProducerType) building = new Producer(mapBlock, buildingType, currentKingdom);
        else if (buildingType.specificConstant instanceof MineType) building = new Mine(mapBlock, buildingType, currentKingdom);
        else building = new Building(mapBlock, buildingType, currentKingdom);
        currentKingdom.setBalance((double) -buildingType.getGOLD());
        currentKingdom.setResourceAmount(buildingType.getRESOURCES(),-buildingType.getRESOURCE_NUMBER());
        mapBlock.setBuildings(building);
        currentKingdom.addBuilding(building);
        currentKingdom.setNormalUnitInPosition(buildingType.getWorkerNeeded(), mapBlock, buildingType.getNumberOfWorker());
        changeNonWorkingUnitPosition(buildingType.getWorkerNeeded(), mapBlock, buildingType.getNumberOfWorker());
    }

    public String dropBuilding(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        try {BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));}
        catch (Exception ignored) {return "There is no such a building!";}
        if (BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_")).specificConstant instanceof SiegeType)
            return "There is no such a building!";
        String result = positionValidate(options.get("x"),options.get("y"));
        if (result != null) return result;
        MapBlock mapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if (!currentKingdom.checkOutOfRange(mapBlock.getxPosition(), mapBlock.getyPosition())) return "This block is out of range!";
        BuildingType buildingType = BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));
        if (!mapBlock.getMapBlockType().isBuildable() && !buildingType.equals(BuildingType.IRON_MINE))
            return "You can not build your building here. Please choose another location!";
        if (mapBlock.getBuildings() != null || mapBlock.getSiege() != null) return "This block already has filled with another building!";
        if (buildingType.equals(BuildingType.HEAD_QUARTER) || buildingType.specificConstant instanceof SiegeType) return "You can not buy this building!";
        if (buildingType.getGOLD() > currentKingdom.getBalance()) return "You do not have enough gold to buy this building.";
        if (buildingType.getRESOURCES() != null && buildingType.getRESOURCE_NUMBER() > currentKingdom.getResourceAmount(buildingType.getRESOURCES()))
            return "You do not have enough " + buildingType.getRESOURCES().name().toLowerCase() + " to buy this building.";
        if (currentKingdom.checkForAvailableNormalUnit(buildingType.getWorkerNeeded()) < buildingType.getNumberOfWorker())
            return "There are not enough available worker to put in this building!";
        createBuilding(mapBlock, buildingType);
        result = checkSpecificBuilding(mapBlock, buildingType);
        if (result != null) return result;
        return buildingType.name().toLowerCase().replaceAll("_"," ") + " added successfully to kingdom.";
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

    public String checkUnitValidation(HashMap<String, String> options) {
        UnitType unitType;
        try {unitType = UnitType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));}
        catch (Exception ignored) {return "There is no such unit exist!";}
        String result;
        result = positionValidate(options.get("x"),options.get("y"));
        if (result != null) return result;
        MapBlock mapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if (mapBlock.getUnitByUnitType(unitType).size() == 0) return "There is no such unit found here!";
        return null;
    }

    public String selectUnit(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        String result = checkUnitValidation(options);
        if (result != null) return result;
        MapBlock mapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        UnitType unitType = UnitType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));
        selectedUnit.clear();
        selectedUnit.addAll(mapBlock.getUnitByUnitType(unitType));
        return "unit";
    }

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
        StringBuilder output = new StringBuilder("food list:");
        for (Food food : currentKingdom.getFoods().keySet()) {
            output.append("\n").append(food.name().toLowerCase()).append(": ").append(currentKingdom.getFoods().get(food));
        }
        return output.toString();
    }

    public String setFoodRate(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (options.get("n").matches("-?\\d+")) {
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
        if (options.get("n").matches("-?\\d+")) {
            int rateNumber = Integer.parseInt(options.get("n"));
            if (rateNumber <= 5 && rateNumber >= -5) {
                currentKingdom.setFearRate(rateNumber);
                return "fear rate settled";
            } else return "rate number out of bounds";
        } else return "rate number is not valid";
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

    public int getPopularityByTaxRate(int taxRate) {
        switch(taxRate) {
            case -3 : return 7;
            case -2 : return 5;
            case -1 : return 3;
            case 0  : return 1;
            case 1  : return -2;
            case 2  : return -4;
            case 3  : return -6;
            case 4  : return -8;
            case 5  : return -12;
            case 6  : return -16;
            case 7  : return -20;
            default: return -24;
        }
    }

    public Double getCostByTaxRate(int taxRate) {
        switch (taxRate) {
            case -3 : return 1.0;
            case -2 : return 0.8;
            case -1 : return 0.6;
            case 0  : return 0.0;
            case 1  : return -0.6;
            case 2  : return -0.8;
            case 3  : return -1.0;
            case 4  : return -1.2;
            case 5  : return -1.4;
            case 6  : return -1.6;
            case 7  : return -1.8;
            default: return -2.0;
        }
    }


}