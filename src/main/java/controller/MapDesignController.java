
package controller;

import model.Map;
import model.MapBlock;
import model.MapBlockType;
import model.Tree;
import view.DesignMapMenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.regex.Matcher;

public class MapDesignController {
    private Map gameMap;
    private DesignMapMenu designMapMenu;

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
        if(options.get("t") == null)
            return "please choose a type to change texture";
        MapBlockType mapBlockType;
        if((mapBlockType = MapBlock.findEnumByLandType(options.get("t"))) == null)
            return "no such type available for lands";
        String checkingResult;
        if(options.get("x") == null && options.get("y") == null) {
            ArrayList<Integer> bounds = new ArrayList<>();
            if((checkingResult = checkLocationValidation(options.get("x1") , options.get("y1"))) != null )
                return checkingResult;
            if((checkingResult = checkLocationValidation(options.get("x2") , options.get("y2"))) != null )
                return checkingResult;
            for (String key : options.keySet())
                if(!key.equals("n"))
                    bounds.add(Integer.parseInt(options.get(key)));
            gameMap.changeType(bounds.get(0), bounds.get(1), bounds.get(2), bounds.get(3), mapBlockType);
        }else if(options.get("x") != null && options.get("y") != null){
            if((checkingResult = checkLocationValidation(options.get("x") , options.get("y"))) != null )
                return checkingResult;
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

    private String checkLocationValidation (String xPosition , String yPosition){
        if(xPosition == null || yPosition == null)
            return "you must specify a location";
        if(!xPosition.matches("\\d+") || !yPosition.matches("\\d+"))
            return "please choose digits for location";
        int x = Integer.parseInt(xPosition);
        int y = Integer.parseInt(yPosition);
        if(gameMap.getMapBlockByLocation(x , y) == null)
            return "index out of bounds";
        return null;
    }

    public String clear(HashMap<String , String> options) {
        String checkingResult;
        if((checkingResult = checkLocationValidation(options.get("x") , options.get("y"))) != null )
            return checkingResult;

        int x = Integer.parseInt(options.get("x"));
        int y = Integer.parseInt(options.get("y"));
        gameMap.clearBlock(x,y);
        return "successfully cleared";
    }
    public String dropRock(HashMap<String , String> options) {
        if(options.get("d") == null)
            return "please enter necessary options";
        if(!options.get("d").matches("[nswer]"))
            return "no such direction";
        String checkingResult;
        if((checkingResult = checkLocationValidation(options.get("x") , options.get("y"))) != null )
            return checkingResult;

        int xPosition = Integer.parseInt(options.get("x"));
        int yPosition = Integer.parseInt(options.get("y"));

        gameMap.restrictAccess(xPosition , yPosition , options.get("d").charAt(0));
        return "successfully dropped";
    }
    public String dropTree(HashMap<String , String> options) {
        if(options.get("t") == null)
            return "please choose a tree";
        Tree tree;
        if((tree = Tree.findEnumByName(options.get("t"))) == null)
            return "no such type available for lands";
        String checkingResult;
        if((checkingResult = checkLocationValidation(options.get("x") , options.get("y"))) != null )
            return checkingResult;

        gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")), Integer.parseInt(options.get("y"))).addTree(tree);
        return "successfully dropped";
    }

    public String addUserToMap(HashMap<String , String> options){
        return null;
    }

    public String dropUnit(HashMap<String , String> options){
        return null;
    }

    public String dropBuilding(HashMap<String , String> options){
        return null;
    }

}

