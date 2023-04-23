package controller;

import model.Map;
import view.DesignMapMenu;

import java.util.regex.Matcher;

public class MapDesignController {
    private Map gameMap;
    private DesignMapMenu designMapMenu;

    public MapDesignController(Map gameMap) {
        this.gameMap = gameMap;
        designMapMenu = new DesignMapMenu(this);
    }

    public void run(){
        switch (designMapMenu.run()){
            case "start":
                GameController gameController = new GameController(gameMap);
                gameController.run();
                break;
            case "back":
                return;
        }
    }

    public String setTexture(Matcher matcher) {
        return null;
    }
    public String clear(Matcher matcher) {
        return null;
    }
    public String dropRock(Matcher matcher) {
        return null;
    }
    public String dropTree(Matcher matcher) {
        return null;
    }

}
