package controller;

import model.Database;
import model.Map;
import model.User;
import view.*;
import java.util.*;

public class Controller {
    private final MainMenu mainMenu;
    public static User currentUser = null;
    public static User loggedInUser = null;
    private Map gameMap;

    public Controller() {
        this.mainMenu = new MainMenu(this);
        Database.setArrayOfUsers();
        for (User user : User.users) if (user.getLoggedIn()) currentUser = loggedInUser = user;
    }

    public void run() {
        LoginController loginController = new LoginController();
        if (currentUser == null) if (loginController.run().equals("exit")) return;
        while (true) {
            switch (mainMenu.run()) {
                case "selectMap":
                    MapDesignController mapDesignController = new MapDesignController(gameMap);
                    mapDesignController.run();
                    break;
                case "profile":
                    ProfileController profileController = new ProfileController();
                    profileController.run();
                    break;
                case "previous map":
                    GameController gameController = new GameController(gameMap);
                    gameController.run();
                    break;
                case "logout":
                    if (loginController.run().equals("exit")) return;
                    break;
            }
        }
    }

    public String showDefaultMaps(){
        Map.createDefaultMaps();
        StringBuilder result = new StringBuilder();
        int counter = 0;
        for (Map defaults: Map.DEFAULT_MAPS) {
            counter++;
            result.append(counter).append(". ").append(defaults.getMapName()).append(": ")
                    .append(defaults.getMapWidth()).append(" * ")
                    .append(defaults.getMapHeight()).append("\n");
        }
        result.deleteCharAt(result.length() - 1);
        return result.toString();
    }

    public String selectDefaultMap(String selectedIndex){
        try{
            int index = Integer.parseInt(selectedIndex);
            gameMap = Map.getDefaultMap(index - 1);
            if(gameMap == null) return "invalid number";
            return "Map was created successfully!";
        }
        catch (Exception IllegalArgumentException){
            return "invalid input, please select a number";
        }
    }

    public String createNewMap(HashMap<String, String> options){
        if(options.get("x") == null || options.get("y") == null)
            return "you must choose width and height";
        if (options.get("x").equals("") || options.get("y").equals(""))
            return "Please input width & height correctly ";
        if(options.get("n") == null || options.get("n").equals(""))
            return "you must choose a name for your map. use -n.";
        int width = Integer.parseInt(options.get("x"));
        int height = Integer.parseInt(options.get("y"));
        if(width < 0 || height < 0) return "invalid bounds!";
        gameMap = new Map(width, height, options.get("n"));
        currentUser.addToMyMap(gameMap);
        return "Map was created successfully!";
    }

    public String chooseFromMyMap() {
        int counter = 0;
        if (currentUser.getMyMap().size() == 0) return "You do not have any map from past";
        StringBuilder stringBuilder = new StringBuilder();
        for (Map map : currentUser.getMyMap()) {
            stringBuilder.append(counter + 1).append(". ")
                    .append(map.getMapName()).append(": ")
                    .append(map.getMapWidth()).append(" * ")
                    .append(map.getMapHeight());
        }
        return stringBuilder.toString();
    }
    public String chooseNumber(String command) {
        if (!command.matches("-?\\d+")) return "Please input a digit as your value.";
        if (Integer.parseInt(command) > currentUser.getMyMap().size() || Integer.parseInt(command) < 1) return "Invalid bounds!";
        gameMap = currentUser.getMyMap().get(Integer.parseInt(command) - 1);
        return "start";
    }

    public String pourOil() {
        return null;
    }
    public String digTunnel() {
        return null;
    }
    public String disbandUnit() {
        return null;
    }

}
