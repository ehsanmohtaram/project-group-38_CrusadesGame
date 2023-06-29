package controller;

import model.Map;
import model.MapBlock;
import model.User;
import model.unit.Unit;
import view.MapMenu;
import java.util.HashMap;

public class MapController {
    private final Map gameMap;
    private final MapMenu mapMenu;
    private int currentX;
    private int currentY;

    public MapController(Map gameMap, User currentUser, int currentX, int currentY) {
        this.gameMap = gameMap;
        this.currentX = currentX;
        this.currentY = currentY;
        mapMenu = new MapMenu(this);
    }

    public void run(){
        mapMenu.run();
    }

    public String moveMap(HashMap<String, String> options){
        HashMap<String , String> validOptions = new HashMap<>();
        for (String key: options.keySet()) {
            if(options.get(key) != null) {
                if(!options.get(key).matches("\\d+") && !options.get(key).equals(""))
                    return "input numbers for directions";
                else
                    validOptions.put(key, options.get(key));
            }
        }
        if(validOptions.size() == 0)
            return "you must specify direction";
        if(validOptions.size() > 2)
            return "please specify one or two directions";
        for (String key: validOptions.keySet()) {
            if(validOptions.get(key).equals("")) {
                if(!setNewLocation(key , 1))
                    return "you reached end of map";
            }else
                if(!setNewLocation(key, Integer.parseInt(validOptions.get(key))))
                    return "you reached end of map";
        }
        return gameMap.getPartOfMap(currentX, currentY);
    }

    private boolean setNewLocation(String direction , int amount){
        switch (direction){
            case "u":
                if(amount > currentY)
                    return false;
                currentY -= amount;
                break;
            case "d":
                if((amount + currentY) > gameMap.getMapHeight())
                    return false;
                currentY += amount;
                break;
            case "l":
                if(amount > currentX)
                    return false;
                currentX -= amount;
                break;
            case "r":
                if((amount + currentX) > gameMap.getMapWidth())
                    return false;
                currentX += amount;
                break;
        }
        return true;
    }

    public String showDetails(HashMap<String, String> options){
        if(options.get("x") == null || options.get("y") == null)
            return "you must specify a location";
        if(!options.get("x").matches("\\d+") || !options.get("x").matches("\\d+"))
            return "please choose digits for location";
        int xPosition = Integer.parseInt(options.get("x"));
        int yPosition = Integer.parseInt(options.get("y"));
        MapBlock detailsWanted;
        if((detailsWanted = gameMap.getMapBlockByLocation(xPosition , yPosition)) == null)
            return "index out of bounds";
        return detailsWanted.showDetails();
    }

}
