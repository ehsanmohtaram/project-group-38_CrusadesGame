package controller;

import model.Map;
import model.MapBlock;
import model.MapBlockType;
import model.User;
import model.unit.Unit;
import view.MapMenu;

import java.util.HashMap;

public class MapController {

    private final Map gameMap;
    private final User currentUser;
    private final MapMenu mapMenu;
    private int currentX;
    private int currentY;

    public MapController(Map gameMap, User currentUser) {
        this.gameMap = gameMap;
        this.currentUser = currentUser;
        this.currentX = currentX;
        this.currentY = currentY;
        mapMenu = new MapMenu(this);
    }

    public void run(){
        mapMenu.run();
    }

    public String moveMap(HashMap<String, String> options){
        HashMap<String , Integer> validOptions = new HashMap<>();
        for (String key: options.keySet()) {
            if(options.get(key) != null) {
                if(!options.get(key).matches("\\d+") && !options.get(key).equals(""))
                    return "input numbers for directions";
            }
            else
                options.remove(key);
        }
        if(options.size() == 0)
            return "you must specify location";
        if(options.size() > 2)
            return "please specify one or two directions";
        for (String key: options.keySet()) {
            if(options.get(key).equals("")) {
                setNewLocation(key , 1);
            }else
                setNewLocation(key, Integer.parseInt(options.get(key)));
        }
        gameMap.getPartOfMap(currentX, currentY);
        return "successful";
    }

    private void setNewLocation(String direction , int amount){
        switch (direction){
            case "u":
                currentY -= amount;
                break;
            case "d":
                currentY += amount;
                break;
            case "l":
                currentX -= amount;
                break;
            case "r":
                currentX += amount;
                break;
        }
    }

    public String showDetails(HashMap<String, String> options){
        if(options.get("x") == null || options.get("y") == null)
            return "you must specify a location";
        if(!options.get("x").matches("\\d+") || !options.get("x").matches("\\d+"))
            return "please choose digits for location";
        int xPosition = Integer.parseInt(options.get("x"));
        int yPosition = Integer.parseInt(options.get("x"));
        MapBlock detailsWanted;
        if((detailsWanted = gameMap.getMapBlockByLocation(xPosition , yPosition)) == null)
            return "index out of bounds";
        StringBuilder result = new StringBuilder("type: "
                + detailsWanted.getMapBlockType().name().toLowerCase().replaceAll("-", " ") + '\n');
        result.append("Units:\n");
        for (Unit unit: detailsWanted.getUnits()) {
            result.append(unit.getUnitType().name().toLowerCase().replaceAll("-", " ")).append(" -> owner: ")
                    .append(unit.getOwner().getFlag().name()).append('\n') ;
        }
        result.append("building:");
        if(detailsWanted.getBuildings() != null){
            result.append(detailsWanted.getBuildings().getBuildingType().name().toLowerCase().replaceAll("-", " "))
                    .append(" -> owner: ")
                    .append(detailsWanted.getBuildings().getOwner().getFlag().name()).append('\n') ;
        }
        if(detailsWanted.getResources() != null){
            result.append(detailsWanted.getResourceAmount()).append(" unit of ").append(detailsWanted.getResources().name().toLowerCase());
        }
        return result.toString();
    }

}
