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

    public boolean backToMainMenu() {
        if (currentUser.equals(Controller.loggedInUser)) return true;
        else return false;
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

    public String showPopularityFactors() {
        return "Popularity factors:\nFood => " + showFoodRate() + "\nTax rate => " + showTaxRate() +
                "\nReligion => " + showReligion() + "\nFear rate => " + showFearRate();
    }

    public String showPopularity() {
        return "Popularity rate : " + currentKingdom.getPopularity();
    }

    public String showFoodList() {
        StringBuilder output = new StringBuilder("Food list: ");
        for (Food food : currentKingdom.getFoods().keySet()) {
            output.append("\n").append(food.name().toLowerCase()).append(" : ").append(currentKingdom.getFoods().get(food));
        }
        return output.toString();
    }

    public String setFoodRate(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (!options.get("r").matches("-?\\d+"))  return "Please input digit as rate!";
        int rateNumber = Integer.parseInt(options.get("r"));
        if (rateNumber > 2 || rateNumber < -2) return "Invalid bound!";
        currentKingdom.setFoodRate(rateNumber);
        int getFood = (int) (((double)(currentKingdom.getFoodRate() + 2) / 2) * (double)currentKingdom.getPopulation());
        int foodCounter = 0;
        for (Food food : currentKingdom.getFoods().keySet()) foodCounter += currentKingdom.getFoods().get(food);
        if (foodCounter - getFood < 0) {
            currentKingdom.setFoodRate(-2);
            return "Food rate set to -2 automatically";
        }
        else {
            currentKingdom.setTaxRate(getFood);
            return "Food rate settled successfully!";
        }
    }

    public String showFoodRate() {
        return "Food rate : " + currentKingdom.getFoodRate();
    }

    public String showReligion() {
        int counter = 0;
        BuildingType buildingType;
        for (Building building : currentKingdom.getBuildings()) {
            buildingType = building.getBuildingType();
            if (buildingType.equals(BuildingType.CATHEDRAL) || buildingType.equals(BuildingType.CHURCH))
                counter++;
        }
        return "Religious building : " + counter;
    }

    public String setTaxRate(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (!options.get("r").matches("-?\\d+"))  return "Please input digit as rate!";
        int taxRate = Integer.parseInt(options.get("r"));
        if (taxRate > 8 || taxRate < -3) return "Invalid bound!";
        double getTax;
        if (currentKingdom.getTaxRate() < 0) getTax = currentKingdom.getTaxRate() * 0.2 - 0.4;
        else getTax = currentKingdom.getTaxRate() * 0.2 + 0.4;
        if (currentKingdom.getBalance() + getTax * currentKingdom.getPopulation() < 0) {
            currentKingdom.setTaxRate(0);
            return "Tax rate set to 0 automatically";
        }
        else {
            currentKingdom.setTaxRate(taxRate);
            return "Tax rate settled successfully!";
        }
    }

    public String showTaxRate() {
        return "Tax rate : " + currentKingdom.getTaxRate();
    }

    public String setFearRate(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet())
            if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (!options.get("r").matches("-?\\d+")) return "please in ut digit as your rate!";
        int rateNumber = Integer.parseInt(options.get("r"));
        if (rateNumber > 5 || rateNumber < -5) return "Invalid bounds!";
        currentKingdom.setFearRate(rateNumber);
        return "fear rate settled successfully!";
    }

    public String showFearRate() {
        return "Fear rate : " + currentKingdom.getFearRate();
    }

}