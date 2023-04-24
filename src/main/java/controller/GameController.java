package controller;

import model.Map;
import view.GameMenu;

public class GameController {
    private Map gameMap;
    private GameMenu gameMenu;

    public GameController(Map gameMap) {
        this.gameMap = gameMap;
        gameMenu = new GameMenu(this);
    }

    public void run(){
        gameMenu.run();
    }

    public String nextTurn(){
        return null;
    }

}
