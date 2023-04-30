package controller;

import model.*;
import view.*;

import java.util.HashMap;

public class GameController {
    public static Map gameMap;
    private final GameMenu gameMenu;
    private final UnitMenu unitMenu;
    private final User currentUser;
    private Kingdom currentKingdom;
    private int XofMap;
    private int YofMap;

    public GameController(Map gameMap) {
        GameController.gameMap = gameMap;
        this.gameMenu = new GameMenu(this);
        this.unitMenu = new UnitMenu(this);
        currentUser = Controller.currentUser;
        currentKingdom = gameMap.getKingdomByOwner(currentUser);
    }

    public void run(){
        ShopAndTradeController shopAndTradeController = new ShopAndTradeController();
        System.out.println("Welcome to game menu:))");
        while (true) {
            switch (gameMenu.run()) {
                case "map":
                    MapController mapController = new MapController(gameMap, currentUser, XofMap, YofMap);
                    mapController.run();
                    break;
                case "trade":
                    shopAndTradeController.runTrade();
                    break;
                case "shop":
                    shopAndTradeController.runShop();
                    break;
                case "building":
                    BuildingController buildingController = new BuildingController();
                    buildingController.run();
                    break;
                case "unit":
                    unitMenu.run();
                    break;
                case "back": return;
            }
        }
    }

    public String nextTurn(){
        return null;
    }

    public String showMap(HashMap<String, String> options){
        for (String key: options.keySet())
            if(options.get(key) == null)
                return "input necessary options";
        for (String key: options.keySet())
            if(options.get(key).matches("\\d*") )
            return "input numbers as arguments";
        int xPosition = Integer.parseInt(options.get("x")) ;
        int yPosition = Integer.parseInt(options.get("y")) ;
        XofMap = xPosition;
        YofMap = yPosition;
        return gameMap.getPartOfMap(xPosition, yPosition);
    }



}
