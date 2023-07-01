package controller;

import model.*;
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
    public static ArrayList<Unit> currentUnit;
    public static ArrayList<MapBlock> involvedBlocks;


    public UnitController() {
        this.gameMap = GameController.gameMap;
        this.currentKingdom = gameMap.getKingdomByOwner(Controller.currentUser);
        involvedBlocks = new ArrayList<>();
        currentUnit = new ArrayList<>(GameController.selectedUnit);
        for (Unit unit : currentUnit) {
            if (!involvedBlocks.contains(unit.getLocationBlock()))
                involvedBlocks.add(unit.getLocationBlock());
            unit.changeSelection(true);
        }

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

    public String moveMultipleUnits(MapBlock destination, String command){
        if(currentUnit == null)
            return "no selection detected";
        if(currentUnit.size() == 0)
            return "no selection detected";
        updateBlocksInvolved();
        String result; String output = "";
        for (MapBlock origin : involvedBlocks) {
            if(command.equals("Move"))
                result = moveUnit(origin, destination);
            else if(command.equals("Attack"))
                result = attackOnUnit(origin, destination);
            else if(command.equals("Patrol"))
                result = patrolUnit(origin, destination);
            else
                return output;
            if(!result.equals("successful"))
                output += "in block " + origin.getxPosition() + "*" + origin.getyPosition() + " : " + result + "\n";
        }
        return output;
    }

    public void updateBlocksInvolved(){
        involvedBlocks = new ArrayList<>();
        for (Unit unit : currentUnit)
            if (!involvedBlocks.contains(unit.getLocationBlock()))
                involvedBlocks.add(unit.getLocationBlock());


    }

    public String moveUnit(MapBlock origin, MapBlock destination) {
//        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
//        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
//        String result;
//        result = positionValidate(options.get("x"),options.get("y"));
//        if (result != null) return result;
//        gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
//        if(destination == null)
//            return "invalid location";
        Integer moveLength = 0;
        ArrayList<Unit> blockUnits = origin.getSelectedUnits();
//        if(!blockUnits.get(0).getUnitType().equals(UnitType.ASSASSINS)) {
            if ((moveLength = gameMap.getShortestWayLength(blockUnits.get(0).getXPosition(), blockUnits.get(0).getYPosition(),
                    destination.getxPosition(), destination.getyPosition(), blockUnits.get(0).getMovesLeft())) == null)
                return "units speed limit error";
//        }
//        else
//        if((moveLength = blockUnits.get(0).getOptimizedDistanceFrom(destination.getxPosition(), destination.getyPosition()
//                ,false)) > blockUnits.get(0).getMovesLeft())
//            return "units speed limit error";
        if(destination.getBuildings() instanceof DefensiveStructure){
            DefensiveStructure defensiveDestination = (DefensiveStructure) destination.getBuildings();
            DefensiveStructureType type = (DefensiveStructureType) destination.getBuildings().getSpecificConstant();
            if(defensiveDestination.getCapacity() + blockUnits.size() > type.getUnitsCapacity())
                return "out of space building error";
            for (Unit unit : blockUnits) {
                unit.setHigherElevation(defensiveDestination);
            }
        }

        for (Unit unit : blockUnits) {
            unit.moveTo(destination, moveLength, gameMap.getFinalWay(), gameMap.getMapPane());
            if (unit.getUnitState().equals(UnitState.PATROLLING))
                unit.setUnitState(UnitState.STANDING);
        }

        if(blockUnits.get(0).getUnitType().equals(UnitType.LADDER_MAN))
            for (Direction value : Direction.values())
                if(!gameMap.checkAccess(destination.getxPosition(), destination.getyPosition(), value)) {
                    gameMap.changeAccess(destination.getxPosition(), destination.getyPosition(), value, true);
                    break;
                }

        return "successful";
    }

    public String setSituation(HashMap<String, String> options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        UnitState unitState;
        try {unitState = UnitState.valueOf(options.get("s").toUpperCase().replaceAll(" ","_"));}
        catch (Exception ignored) {return "No such state has been found!";}
//        MapBlock mapBlock = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
//        UnitType unitType = UnitType.valueOf(options.get("t").toUpperCase().replaceAll(" ","_"));
//        currentUnit.clear();
//        currentUnit.addAll(mapBlock.getUnitByUnitType(unitType, currentKingdom));
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
        Unit unit = new Unit(UnitType.valueOf(buildingType.toString()), mapBlock, currentKingdom);
        Camp tent = new Camp(nearestFreeBlock(mapBlock.getxPosition(), mapBlock.getyPosition()), BuildingType.SIEGE_TENT, currentKingdom);
        if (mapBlock.getUnits().size() != 0 ) mapBlock.getUnits().set(0, unit);
        else mapBlock.addUnitHere(unit);
        currentKingdom.addBuilding(tent);
        currentKingdom.addBuilding(siege);
        mapBlock.setSiege(siege);
        MapBlock newMapBlock = nearestFreeBlock(mapBlock.getxPosition(), mapBlock.getyPosition());
        newMapBlock.setBuildings(tent);
        if (!((SiegeType)siege.getBuildingType().specificConstant).getPortable() && mapBlock.getBuildings() != null)
            unit.setHigherElevation((DefensiveStructure) mapBlock.getBuildings());
        for(int i = 0; i < buildingType.getNumberOfWorker(); i++) {
            currentUnit.get(0).setUnitState(UnitState.WORKING);
            mapBlock.addUnitHere(currentUnit.get(0));
            currentUnit.get(0).getLocationBlock().getUnits().remove(currentUnit.get(0));
            currentUnit.get(0).setLocationBlock(mapBlock);
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


    public String attackOnUnit(MapBlock origin, MapBlock target) {
//        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
//        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
//        String result;
//        result = positionValidate(options.get("x"),options.get("y"));
//        if (result != null) return result;
//        MapBlock target = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
//        if(target == null)
//            return "invalid location";
        System.out.println("horra");
        if(target.getUnits().size() == 0 && target.getBuildings() == null)
            return "no enemy detected there";
        ArrayList<Unit> currentUnit = origin.getSelectedUnits();
        ArrayList<Unit> enemy = target.getUnits();
        if(currentUnit.get(0).getUnitType().getCAN_DO_AIR_ATTACK()) {
            if (target.getOptimizedDistanceFrom(currentUnit.get(0).getXPosition(), currentUnit.get(0).getYPosition(), true) >
                    currentUnit.get(0).getOptimizedAttackRange())
                return "Arrows will not go that far";
        }
        else if(target.getOptimizedDistanceFrom(currentUnit.get(0).getXPosition(), currentUnit.get(0).getYPosition(),false) >
                currentUnit.get(0).getOptimizedAttackRange())
            return "out of attack range. first move your units";
        boolean canEnemyAnswer = true;
        if(enemy.get(0).getUnitType().getCAN_DO_AIR_ATTACK()) {
            if (currentUnit.get(0).getLocationBlock().getOptimizedDistanceFrom(enemy.get(0).getXPosition(), enemy.get(0).getYPosition(), true) >
                    enemy.get(0).getOptimizedAttackRange())
                canEnemyAnswer = false;
        }
        else if(currentUnit.get(0).getLocationBlock().getOptimizedDistanceFrom(enemy.get(0).getXPosition(), enemy.get(0).getYPosition(),false) >
                enemy.get(0).getOptimizedAttackRange())
            canEnemyAnswer = false;

        if(target.isUnitsShouldBeAttackedFirst(currentUnit.get(0))){
            outer:
            for (Unit unit : currentUnit)
                for (Unit targetUnit : target.getUnits())
                    if(!unit.bilateralFightTillEnd(targetUnit, GameController.fightBoard, canEnemyAnswer, currentUnit))
                        continue outer;
            battleWithBuilding(target);

        }else {
            battleWithBuilding(target);
        }
        return "successful";
    }

    private void battleWithBuilding(MapBlock target){
        ArrayList<Unit> archers = gameMap.getEnemiesInSurroundingArea(target.getxPosition(), target.getyPosition()
                , currentKingdom, true, 5);
        if(currentUnit.size() > 0 && target.getBuildings() != null) {
            Building building = target.getBuildings();
            if (building.getBuildingType().equals(BuildingType.BIG_STONE_GATEHOUSE) ||
                    building.getBuildingType().equals(BuildingType.SMALL_STONE_GATEHOUSE) ||
                    building.getBuildingType().equals(BuildingType.DRAWBRIDGE))
                gameMap.setGateFlag(building, currentKingdom.getFlag());
            else
                for (Unit unit : currentUnit)
                    unit.destroyBuilding(target.getBuildings(), archers);
        }
    }

    public String patrolUnit(MapBlock origin, MapBlock destination){
//        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
//        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";String result;
//        result = positionValidate(options.get("x1"),options.get("y1"));
//        if (result != null) return result;
//        result = positionValidate(options.get("x2"),options.get("y2"));
//        if (result != null) return result;
//        MapBlock origin = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x1")),Integer.parseInt(options.get("y1")));
//        MapBlock destination = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x2")),Integer.parseInt(options.get("y2")));
//        if(origin == null || destination == null)
//            return "invalid location";
        ArrayList<Unit> currentUnit = origin.getSelectedUnits();
//        if(destination.getBuildings() != null )
//            return "patrol is available just for free locations not in buildings";
        if(currentUnit.get(0).getUnitType().equals(UnitType.TUNNELER))
            return "no patrol option available for tunnelers. you should use them correctly";
//        Integer moveLength;
//        if((moveLength = gameMap.getShortestWayLength(currentUnit.get(0).getXPosition(), currentUnit.get(0).getYPosition(),
//                origin.getxPosition(), origin.getyPosition(), currentUnit.get(0).getMovesLeft())) == null)
//            return "They are too slow to reach such destination";
        Integer lengthBetween;
        if((lengthBetween = gameMap.getShortestWayLength(origin.getxPosition(), origin.getyPosition(),
                destination.getxPosition(), destination.getyPosition(), currentUnit.get(0).getUnitType().getVELOCITY())) == null)
            return "They are too slow to move between such origin & destination";

        for (Unit unit : currentUnit) {
//            unit.moveTo(origin, moveLength);
            unit.setUnitState(UnitState.PATROLLING);
            unit.setPatrolDestination(destination);
        }
        return "successful";
    }

    public String digTunnel(HashMap<String, String> options){
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        MapBlock target = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if(target == null)
            return "invalid location";
        Building toDestroy;
        if(!currentUnit.get(0).getUnitType().equals(UnitType.TUNNELER))
            return "tunnelers just can dig tunnels";
        if((toDestroy = target.getBuildings()) == null)
            return "there is no building there to dig tunnel under it";
        if(toDestroy.getOwner().equals(currentUnit.get(0).getOwner()))
            return "Are you crazy??? you can not destroy your own building with tunnelers";
        if(!(toDestroy instanceof DefensiveStructure))
            return "you can not dig tunnel under non defensive structures";
        DefensiveStructure defensiveStructure = (DefensiveStructure) toDestroy;
        DefensiveStructureType type = (DefensiveStructureType)defensiveStructure.getSpecificConstant();
        if(!type.getTunnelImmune())
            return "you can not dig tunnel under specific towers";
        Integer moveLength;
        if((moveLength = gameMap.getShortestWayLength(currentUnit.get(0).getXPosition(), currentUnit.get(0).getYPosition(),
                target.getxPosition(), target.getyPosition(), currentUnit.get(0).getMovesLeft())) == null)
            return "They are too slow to reach such destination";
        for (Unit unit : currentUnit) {
            unit.moveTo(target, moveLength, gameMap.getFinalWay(), gameMap.getMapPane());
            if(unit.getUnitType().equals(UnitState.PATROLLING))
                unit.setUnitState(UnitState.STANDING);
        }

        for (Unit defender : gameMap.getEnemiesInSurroundingArea(target.getxPosition(), target.getyPosition()
                , currentKingdom, false, 5))
            for (Unit attacker : currentUnit)
                defender.bilateralFight(attacker, true);
        if(checkRemainingUnits())
            toDestroy.decreaseHP(toDestroy.getHp());
        return "they are sent to do their work";
    }

    public String digTrench(HashMap<String, String> options){
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        String result;
        result = positionValidate(options.get("x"),options.get("y"));
        if (result != null) return result;
        MapBlock target = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if(target == null)
            return "invalid location";
        if(!target.getMapBlockType().canBeDug())
            return "you can not dig in this types of land";
        Integer moveLength;
        if((moveLength = gameMap.getShortestWayLength(currentUnit.get(0).getXPosition(), currentUnit.get(0).getYPosition(),
                target.getxPosition(), target.getyPosition(), currentUnit.get(0).getMovesLeft())) == null)
            return "They are too slow to reach such destination";
        for (Unit unit : currentUnit) {
            unit.moveTo(currentUnit.get(0).getLocationBlock(), moveLength, gameMap.getFinalWay(), gameMap.getMapPane());
            if (unit.getUnitState().equals(UnitState.PATROLLING))
                unit.setUnitState(UnitState.STANDING);
        }
        gameMap.changeType(target.getxPosition(), target.getyPosition(), target.getxPosition(), target.getyPosition(), MapBlockType.HOLE);
        return "they are doing their work";
    }

    public String fillTrench(HashMap<String, String> options){
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        String result;
        result = positionValidate(options.get("x"),options.get("y"));
        if (result != null) return result;
        MapBlock target = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if(target == null)
            return "invalid location";
        if(!target.getMapBlockType().equals(MapBlockType.HOLE))
            return "there is no hole there";
        Integer moveLength;
        if((moveLength = gameMap.getShortestWayLength(currentUnit.get(0).getXPosition(), currentUnit.get(0).getYPosition(),
                target.getxPosition(), target.getyPosition(), currentUnit.get(0).getMovesLeft())) == null)
            return "They are too slow to reach such destination";
        for (Unit unit : currentUnit) {
            unit.moveTo(currentUnit.get(0).getLocationBlock(), moveLength, gameMap.getFinalWay(), gameMap.getMapPane());
            if (unit.getUnitState().equals(UnitState.PATROLLING))
                unit.setUnitState(UnitState.STANDING);
        }
        gameMap.changeType(target.getxPosition(), target.getyPosition(), target.getxPosition(), target.getyPosition(), MapBlockType.EARTH);
        return "they are doing their work";
    }

    public String pourOil(HashMap<String, String> options){
        if(options.get("d") == null) return "please enter necessary options";
        if(!options.get("d").matches("[nswe]")) return "no such direction";
        int x = 0 , y = 0;
        if(currentKingdom.getResourceAmount(ResourceType.OIL) <= 0)
            return "not enough oil in stock";
        switch (options.get("d")){
            case "n":
                x = currentUnit.get(0).getXPosition();
                y = currentUnit.get(0).getYPosition() - 1;
                break;
            case "s":
                x = currentUnit.get(0).getXPosition();
                y = currentUnit.get(0).getYPosition() + 1;
                break;
            case "w":
                x = currentUnit.get(0).getXPosition() - 1;
                y = currentUnit.get(0).getYPosition();
                break;
            case "e":
                x = currentUnit.get(0).getXPosition() + 1;
                y = currentUnit.get(0).getYPosition();
                break;
        }
        MapBlock location = gameMap.getMapBlockByLocation(x , y);
        for (Unit unit : location.getUnits()) {
            for (Unit engineer : currentUnit) {
                engineer.bilateralFight(unit, false);
            }
        }
        currentKingdom.setResourceAmount(ResourceType.OIL, -2);
        ArrayList<Unit> archers = gameMap.getEnemiesInSurroundingArea(location.getxPosition(), location.getyPosition()
                , currentKingdom, true, 5);
        if(currentUnit.size() > 0)
            for (Unit unit : currentUnit) {
                unit.destroyBuilding(location.getBuildings(), archers);
            }
        return "Oil pour successfully!";
    }

    public String fireTrench(HashMap<String, String> options){
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        String result;
        result = positionValidate(options.get("x"),options.get("y"));
        if (result != null) return result;
        MapBlock target = gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")));
        if(target == null)
            return "invalid location";
        if(!target.getTrap().getTrapType().equals(TrapType.BITUMEN_TRENCH))
            return "there is no trench there";
        if(currentUnit.get(0).getUnitType().getCAN_DO_AIR_ATTACK())
            return "selected units are not archer";
        Integer moveLength = 0;
        if (target.getOptimizedDistanceFrom(currentUnit.get(0).getXPosition(), currentUnit.get(0).getYPosition(), true) >
                currentUnit.get(0).getOptimizedAttackRange())
            return "Arrows will not go that far";

        target.getTrap().setActive(true);
        return "fire will destroy anyone!";
    }

    public String disband(){
        currentKingdom.setNoneEmployed(currentUnit.size());
        for (Unit unit : currentUnit) {
            unit.removeUnit();
        }
        return "they go back to their origin!";
    }

    public boolean checkRemainingUnits(){
        return currentUnit.size() != 0;
    }


    public void freeSelection() {
        for (Unit unit : currentUnit) {
            unit.changeSelection(false);
        }
    }
}
