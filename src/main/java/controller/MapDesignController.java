
package controller;

import model.Map;
import model.MapBlock;
import model.MapBlockType;
import view.DesignMapMenu;

import java.util.ArrayList;
import java.util.HashMap;

public class MapDesignController {
    private final Map gameMap;
    private final DesignMapMenu designMapMenu;

    public MapDesignController(Map gameMap) {
        this.gameMap = gameMap;
        designMapMenu = new DesignMapMenu(this);
    }

    public void run(){
        if(designMapMenu.run().equals("start")){
            GameController gameController = new GameController(gameMap);
            gameController.run();
        }
    }

    public String setTexture(HashMap<String , String> options) {
        MapBlockType mapBlockType;
        if((mapBlockType = MapBlock.findEnumByLandType(options.get("t"))) == null)
            return "no such type available for lands";
        if(options.get("x") == null && options.get("y") == null) {
            ArrayList<Integer> bounds = new ArrayList<>();
            options.remove("x");
            options.remove("y");
            for (String key : options.keySet()) {
                if (!key.equals("n") && !options.get(key).matches("\\d+"))
                    return "please choose a digit for bounds";
                else if(!key.equals("n"))
                    bounds.add(Integer.parseInt(options.get(key)));
            }
            gameMap.changeType(bounds.get(0), bounds.get(1), bounds.get(2), bounds.get(3), mapBlockType);
        }else if(options.get("x") != null && options.get("y") != null){
            if(!options.get("x").matches("\\d+") || !options.get("y").matches("\\d+"))
                return "please choose digits for location";
            int x = Integer.parseInt(options.get("x"));
            int y = Integer.parseInt(options.get("y"));
            options.remove("x");
            options.remove("y");
            for (String key : options.keySet())
                if (!key.equals("n") && options.get(key) != null)
                    return "choose two or four digits to specify area!";
            gameMap.changeType(x, y, mapBlockType);
        }else
            return "you must choose at least two digits for bounds";
        return "type changed successfully";
    }

    public String clear(HashMap<String , String> options) {
        if(options.get("x") == null || options.get("y") == null)
            return "you must specify a location";
        if(!options.get("x").matches("\\d+") || !options.get("y").matches("\\d+"))
            return "please choose digits for location";
        int x = Integer.parseInt(options.get("x"));
        int y = Integer.parseInt(options.get("y"));
        gameMap.clearBlock(x,y);
        return null;
    }
    public String dropRock(HashMap<String , String> options) {
        return null;
    }
    public String dropTree(HashMap<String , String> options) {
        return null;
    }

}

