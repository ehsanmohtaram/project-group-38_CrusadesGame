
package controller;

import model.*;
import model.building.Building;
import model.building.BuildingType;
import view.DesignMapMenu;
import java.util.ArrayList;
import java.util.HashMap;


public class MapDesignController {
    private final Map gameMap;
    private final DesignMapMenu designMapMenu;
    private final User currentUser;
    private int XofMap;
    private int YofMap;

    public MapDesignController(Map gameMap) {
        this.gameMap = gameMap;
        designMapMenu = new DesignMapMenu(this);
        currentUser = Controller.currentUser;
    }

    public void run(){
        while (true){
            switch (designMapMenu.run()) {
                case "start":
                    GameController gameController = new GameController(gameMap);
                    gameController.run();
                    return;
                case "map":
                    MapController mapController = new MapController(gameMap, currentUser, XofMap, YofMap);
                    mapController.run();

            }
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
            int x1 = Integer.parseInt(options.get("x1"));
            int x2 = Integer.parseInt(options.get("x2"));
            int y1 = Integer.parseInt(options.get("y1"));
            int y2 = Integer.parseInt(options.get("y2"));
            gameMap.changeType(x1, y1, x2, y2, mapBlockType);
        }else if(options.get("x") != null && options.get("y") != null){
            if((checkingResult = checkLocationValidation(options.get("x") , options.get("y"))) != null )
                return checkingResult;
            int x = Integer.parseInt(options.get("x"));
            int y = Integer.parseInt(options.get("y"));
            options.remove("x");
            options.remove("y");
            for (String key : options.keySet())
                if (!key.equals("t") && options.get(key) != null)
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

        if(!gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")), Integer.parseInt(options.get("y"))).addTree(tree))
            return "here is not cultivable";
        return "successfully dropped";
    }

    public String checkAroundHeadQuarterPosition(Integer xPosition, Integer yPosition) {
        int changeXToString;
        int changeYToString;
        for (int i = -10; i < 10; i++) {
            for (int j = -10; j < 10; j++) {
                changeXToString = xPosition + i;
                changeYToString = yPosition + j;
                if (checkLocationValidation(Integer.toString(changeXToString), Integer.toString(changeYToString)) != null)
                    return "You have to put your HeadQuarter in a suitable position!";
                if (gameMap.getMapBlockByLocation(changeXToString,changeYToString).getBuildings() != null) return "You can not make your HeadQuarter next to others!";
            }
        }
        return null;
    }

    public String startPlaying(){
        if (gameMap.getKingdomByOwner(currentUser) == null) return "You have not added yourself to map yet. Please add your kingdom!";
        if (gameMap.getPlayers().size() < 2) return "Please at least add 2 players to start game!";
        return "start";
    }

    public String addUserToMap(HashMap<String , String> options){
        String checkingResult;
        for (String key : options.keySet())
            if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (!options.get("x").matches("-?\\d+") || !options.get("y").matches("-?\\d+"))
            return "Please enter digits as value of X and Y position!";
        if (User.getUserByUsername(options.get("u")) == null) return "User dose not exist. Please choose user from registered users";
        try {Flags.valueOf(options.get("f").toUpperCase());}
        catch (IllegalArgumentException illegalArgumentException)
        {return "Your flag color did not exist in default colors!";}
        if((checkingResult = checkLocationValidation(options.get("x") , options.get("y"))) != null ) return checkingResult;
        if (!gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y"))).getMapBlockType().isAccessible())
            return "You can not build your head quarter in this type of land!";
        if ((checkingResult = checkAroundHeadQuarterPosition(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y")))) != null)
            return checkingResult;
        if (gameMap.getKingdomByOwner(User.getUserByUsername(options.get("u"))) != null)
            return "You already have put your kingdom in this map!";
        for (Kingdom kingdom : gameMap.getPlayers())
            if (kingdom.getFlag().name().equals(options.get("f").toUpperCase())) return "Another user choose this flag. Please choose another flag.";
        Kingdom kingdom = new Kingdom(Flags.valueOf(options.get("f").toUpperCase()),
                User.getUserByUsername(options.get("u")));
        Building headQuarter = new Building(gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y"))), BuildingType.HEAD_QUARTER, kingdom);
        kingdom.setHeadquarter(headQuarter);
        gameMap.addPlayer(kingdom);
        gameMap.getMapBlockByLocation(Integer.parseInt(options.get("x")),Integer.parseInt(options.get("y"))).setBuildings(building);
        User.getUserByUsername(options.get("u")).addToMyMap(gameMap);
        return "User add to map successfully!";
    }

    public String dropUnit(HashMap<String , String> options){
        return null;
    }

    public String dropBuilding(HashMap<String , String> options){
        return null;
    }

    public String showMap(HashMap<String, String> options){
        String checkingResult;
        if((checkingResult = checkLocationValidation(options.get("x") , options.get("y"))) != null )
            return checkingResult;
        int xPosition = Integer.parseInt(options.get("x")) ;
        int yPosition = Integer.parseInt(options.get("y")) ;
        XofMap = xPosition;
        YofMap = yPosition;
        return gameMap.getPartOfMap(xPosition, yPosition);
    }

}

