package controller;

import model.*;
import view.*;

public class GameController {
    public static Map gameMap;
    private final GameMenu gameMenu;
    private final UnitMenu unitMenu;
    private final User currentUser;

    public GameController(Map gameMap) {
        GameController.gameMap = gameMap;
        this.gameMenu = new GameMenu(this);
        this.unitMenu = new UnitMenu(this);
        currentUser = Controller.currentUser;
    }

    public void run(){
        ShopAndTradeController shopAndTradeController = new ShopAndTradeController();
        System.out.println("Welcome to game menu:))");
        while (true) {
            switch (gameMenu.run()) {
                case "map":
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

}
