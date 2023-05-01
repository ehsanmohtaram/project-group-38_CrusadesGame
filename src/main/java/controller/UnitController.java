package controller;

import model.Kingdom;
import model.Map;
import model.User;
import model.unit.Unit;
import model.unit.UnitState;
import model.unit.UnitType;
import view.UnitMenu;

import java.util.HashMap;

public class UnitController {
    private final Map gameMap;
    private final Kingdom currentKingdom;
    private final UnitMenu unitMenu;
    private final Unit currentUnit;

    public UnitController(Map gameMap, Kingdom currentKingdom, Unit currentUnit) {
        this.gameMap = gameMap;
        this.currentKingdom = currentKingdom;
        this.currentUnit = currentUnit;
        this.unitMenu = new UnitMenu(this);
    }

    public void run(){
        unitMenu.run();
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
                        if (unit.getOwner().equals(currentKingdom)) {
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

    } //TODO این سه تا دستور یونیت شباهت خیلی زیادی دارن. میشه یه تابع برا اروراش زد

}
