package controller;

import model.Kingdom;
import model.Map;
import model.MapBlock;
import model.building.*;
import model.unit.Unit;
import model.unit.UnitState;
import model.unit.UnitType;
import view.UnitMenu;

import java.util.ArrayList;
import java.util.HashMap;

public class UnitController {
    private final Map gameMap;
    private final Kingdom currentKingdom;
    private final UnitMenu unitMenu;
    private final ArrayList<Unit> currentUnit = new ArrayList<>();

    public UnitController() {
        this.gameMap = GameController.gameMap;
        this.currentKingdom = gameMap.getKingdomByOwner(Controller.currentUser);
        currentUnit.addAll(GameController.selectedUnit);
        this.unitMenu = new UnitMenu(this);
    }

    public void run(){
        unitMenu.mainRun();
    }


    public void redirect() {
        UnitType unitType = currentUnit.get(0).getUnitType();
        if (unitType.equals(UnitType.ENGINEER)) unitMenu.runEngineer();
        else if (unitType.equals(UnitType.WORKER)) unitMenu.runWorker();
        else unitMenu.runUnit();
    }

    //TODO move unit


    public String moveUnit(HashMap<String, String > options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        MapBlock destination = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if(destination == null)
            return "invalid location";
        Integer moveLength;
        if((moveLength = gameMap.getShortestWayLength(currentUnit.get(0).getXPosition(), currentUnit.get(0).getYPosition(),
                destination.getxPosition(), destination.getyPosition(), currentUnit.get(0).getMovesLeft())) == null)
            return "they are too slow to reach such destination";
        for (Unit unit : currentUnit)
            unit.moveTo(destination, moveLength);
        return "moved successfully";
    }

    public String setSituation(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        UnitState unitState;
        try {unitState = UnitState.valueOf(options.get("s").toUpperCase().replaceAll(" ","_"));}
        catch (Exception ignored) {return "No such state has been found!";}
        MapBlock mapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        UnitType unitType = UnitType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));
        currentUnit.clear();
        currentUnit.addAll(mapBlock.getUnitByUnitType(unitType));
        for (Unit unit : currentUnit) unit.setUnitState(unitState);
        return "Unit states change successfully!";
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


    public MapBlock nearestFreeBlock(int x, int y) {
        MapBlock mapBlock;
        for (int i = -4; i < 4; i++) {
            if ( i + x  > gameMap.getMapWidth() || i + x < 0) continue;
            for (int j = -4; j < 4; j++) {
                if ( j + y  > gameMap.getMapHeight() || j + y < 0) continue;
                mapBlock = gameMap.getMapBlockByLocation(x + i, y + j);
                if (mapBlock.getBuildings() == null && mapBlock.getSiege() == null && mapBlock.getMapBlockType().isBuildable())
                    return gameMap.getMapBlockByLocation(x + i, y + j);
                }
            }
        return null;
    }

    public void createSiege(BuildingType buildingType, MapBlock mapBlock) {
        currentKingdom.setBalance((double) -buildingType.getGOLD());
        Siege siege = new Siege(mapBlock ,buildingType, currentKingdom);
        Camp tent = new Camp(nearestFreeBlock(mapBlock.getxPosition(), mapBlock.getyPosition()), BuildingType.SIEGE_TENT, currentKingdom);
        currentKingdom.addBuilding(tent);
        currentKingdom.addBuilding(siege);
        mapBlock.setSiege(siege);
        nearestFreeBlock(mapBlock.getxPosition(), mapBlock.getyPosition()).setBuildings(tent);
        for(int i = 0; i < buildingType.getNumberOfWorker() - 1; i++) {
            mapBlock.addUnitHere(currentUnit.get(0));
            currentUnit.get(0).setLocationBlock(mapBlock);
            currentUnit.get(0).getLocationBlock().getUnits().remove(currentUnit.get(0));
            currentUnit.remove(0); GameController.selectedUnit.remove(0);
        }
    }

    public String dropSiege(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        BuildingType buildingType;
        try {buildingType = BuildingType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));}
        catch (Exception ignored) {return "There is no such a siege!";}
        if (!(buildingType.specificConstant instanceof SiegeType)) return "There is no such a siege!";
        SiegeType siegeType = (SiegeType) buildingType.specificConstant;
        String result = positionValidate(options.get("x"),options.get("y"));
        if (result != null) return result;
        MapBlock mapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if (buildingType.getNumberOfWorker() > currentUnit.size()) return "There not enough engineer in your selected unit!";
        if (!currentKingdom.checkOutOfRange(mapBlock.getxPosition(), mapBlock.getyPosition())) return "Position out of range!";
        if (!mapBlock.getMapBlockType().isAccessible()) return "This block is not accessible!";
        if (mapBlock.getBuildings() != null && siegeType.getPortable()) return "This block is already filled with another building!";
        if (mapBlock.getBuildings() != null && !siegeType.getPortable()) {
            if (!mapBlock.getBuildings().getOwner().equals(currentKingdom)) return "You do not own the building in this block!";
            if (!(mapBlock.getBuildings().getSpecificConstant() instanceof DefensiveStructureType)) return "This building did not hold siege!";
            DefensiveStructureType defensiveStructureType = (DefensiveStructureType) mapBlock.getBuildings().getSpecificConstant();
            if (!defensiveStructureType.getHoldSiege()) return "This building did not hold siege!";
        }
        if (currentKingdom.getBalance() < buildingType.getGOLD()) return "You do not have enough gold to buy this building.";
        if (nearestFreeBlock(mapBlock.getxPosition(), mapBlock.getyPosition()) == null) return "There is no available block on map to put tent!";
        createSiege(buildingType, mapBlock);
        return buildingType.name().toLowerCase().replaceAll("_"," ") + " added successfully to your kingdom.";
    }

    //TODO attack unit
    /*
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
                        if (!(unit.getOwner().equals(currentKingdom))) {
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

    }

     */

}
