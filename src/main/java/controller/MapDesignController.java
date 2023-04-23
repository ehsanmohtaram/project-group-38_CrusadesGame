package controller;

import model.Map;
import view.SelectMapMenu;

public class MapDesignController {
    private Map gameMap;
    private SelectMapMenu selectMapMenu;

    public MapDesignController(Map gameMap) {
        this.gameMap = gameMap;
        selectMapMenu = new SelectMapMenu(this);
    }

    public void run(){
        switch (selectMapMenu.run()){
            case "start":
                GameController gameController = new GameController(gameMap);
                gameController.run();
                break;
            case "back":
                return;
        }
    }
}
