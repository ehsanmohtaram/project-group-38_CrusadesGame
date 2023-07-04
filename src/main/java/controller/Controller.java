package controller;

import model.Map;
import model.User;

import java.util.*;

public class Controller {
    public static User currentUser = null;
    public static User loggedInUser = null;
    public Map gameMap;

    public Controller() {
        for (User user : User.users) if (user.getLoggedIn()) currentUser = loggedInUser = user;
    }

    public void run() {
//        LoginController loginController = new LoginController();
//        //if (currentUser == null) if (loginController.run().equals("exit")) return;
//        while (true) {
//            switch (mainMenu.run()) {
//                case "selectMap":
//                    MapDesignController mapDesignController = new MapDesignController(gameMap);
//                    mapDesignController.run();
//                    break;
//                case "profile":
////                    ProfileController profileController = new ProfileController();
////                    profileController.run();
////                    break;
//                case "previous map":
//                    GameController gameController = new GameController(gameMap);
//                    gameController.run();
//                    break;
//                case "logout":
//                    //if (loginController.run().equals("exit")) return;
//                    break;
//            }
//        }
    }

    public ArrayList<String> showDefaultMaps(){
        Map.createDefaultMaps();
        StringBuilder result = null;
        ArrayList<String> outputs = new ArrayList<>();
        int counter = 0;
        for (Map defaults: Map.DEFAULT_MAPS) {
            result = new StringBuilder();
            counter++;
            result.append(counter).append(". ").append(defaults.getMapName()).append(": ")
                    .append(defaults.getMapWidth()).append(" * ")
                    .append(defaults.getMapHeight()).append("\n");
            outputs.add(result.toString());
        }
        result.deleteCharAt(result.length() - 1);
        return outputs;
    }

    public MapDesignController selectMap(int selectedIndex, boolean isDefault){
        try{
            if(isDefault)
                gameMap = Map.getDefaultMap(selectedIndex);
            else
                gameMap = currentUser.getMyMap().get(selectedIndex);
            if(gameMap == null) return null;
            return new MapDesignController(gameMap);
        }
        catch (Exception IllegalArgumentException){
            return null;
        }
    }


    public MapDesignController createNewMap(HashMap<String, String> options){
//        if(options.get("x") == null || options.get("y") == null)
//            return "you must choose width and height";
//        if (options.get("x").equals("") || options.get("y").equals(""))
//            return "Please input width & height correctly ";
//        if(options.get("n") == null || options.get("n").equals(""))
//            return "you must choose a name for your map. double click on name to change it.";
        int width = Integer.parseInt(options.get("x"));
        int height = Integer.parseInt(options.get("y"));
//        if(width < 0 || height < 0) return "invalid bounds!";
        gameMap = new Map(width, height, options.get("n"));
        currentUser.addToMyMap(gameMap);
        return new MapDesignController(gameMap);
    }

    public ArrayList<String> chooseFromMyMap() {
        StringBuilder result = null;
        ArrayList<String> outputs = new ArrayList<>();
        int counter = 0;
        for (Map defaults: currentUser.getMyMap()) {
            result = new StringBuilder();
            counter++;
            result.append(counter).append(". ").append(defaults.getMapName()).append(": ")
                    .append(defaults.getMapWidth()).append(" * ")
                    .append(defaults.getMapHeight()).append("\n");
            outputs.add(result.toString());
        }
//        result.deleteCharAt(result.length() - 1);
        return outputs;
    }
    public String chooseNumber(String command) {
        if (!command.matches("-?\\d+")) return "Please input a digit as your value.";
        if (Integer.parseInt(command) > currentUser.getMyMap().size() || Integer.parseInt(command) < 1) return "Invalid bounds!";
        if (currentUser.getMyMap().get(Integer.parseInt(command) - 1).isEndGame()) return "This game is already Finished!";
        gameMap = currentUser.getMyMap().get(Integer.parseInt(command) - 1);
        return "start";
    }

}
