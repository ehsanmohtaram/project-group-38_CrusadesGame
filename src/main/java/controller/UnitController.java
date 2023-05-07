package controller;

import model.Kingdom;
import model.Map;
import model.MapBlock;
import model.User;
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
        unitMenu.run();
    }

    //TODO move unit

    /*
    public String moveUnit(HashMap<String, String > options) {
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        int x = Integer.parseInt(options.get("x"));
        int y = Integer.parseInt(options.get("y"));
        if (x <= gameMap.getMapWidth() && y <= gameMap.getMapHeight()) {
            if (!(gameMap.getMapBlockByLocation(x, y).getMapBlockType().name().equals("WATER")) ||
                    !(gameMap.getMapBlockByLocation(x, y).getMapBlockType().name().equals("MOUNTAIN")) || (gameMap.getMapBlockByLocation(x, y).getBuildings() != null)) {
                if (currentUnit.getXPosition() - x + currentUnit.getYPosition() - y <= currentUnit.getUnitType().getVELOCITY()) {
                } else
                    return "The speed of the soldier is not enough";
            } else
                return "The soldier can go to that location";
        }
        else
            return "your location out of bounds";
        return null;
    }*/

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
