package controller;

import model.Map;
import model.User;
import view.MapMenu;

import java.util.HashMap;

public class MapController {

    private final Map gameMap;
    private final User currentUser;
    private final MapMenu mapMenu;

    public MapController(Map gameMap, User currentUser) {
        this.gameMap = gameMap;
        this.currentUser = currentUser;
        mapMenu = new MapMenu(this);
    }

    public void run(){
        mapMenu.run();
    }

    public String moveMap(HashMap<String, String> options){
        return null;
    }

}
