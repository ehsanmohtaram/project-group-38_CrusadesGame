package controller;

import model.Database;
import model.Map;
import model.User;
import view.*;
import java.util.*;
import java.util.regex.Matcher;

public class Controller {
    private final MainMenu mainMenu;
    private DesignMapMenu designMapMenu;
    public static User currentUser = null;
    public static boolean stayLoggedIn = false;
    private Map gameMap;

    public Controller() {
        this.mainMenu = new MainMenu(this);
        Database.setArrayOfUsers();
        for (User user : User.users) if (user.getLoggedIn()) currentUser = user;
    }

    public void run() {
        LoginController loginController = new LoginController();
        if (currentUser == null) if (loginController.run().equals("exit")) return;
        while (true) {
            switch (mainMenu.run()) {
                case "selectMap":
//                  currentUser.addToMyMap();
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
        String command;
        if (currentUser.getMyMap().size() == 0) return "You do not have any map from past";
        for (Map map : currentUser.getMyMap()) {
            System.out.println((counter + 1) + ". " + map.getMapName() + ": " + map.getMapWidth() + " * " + map.getMapHeight());
        }
        System.out.print("Please choose one of the following maps to play : ");
        command = CommandParser.getScanner().nextLine();
        if (!command.matches("-?\\d+")) return "Please input a digit as your value.";
        if (Integer.parseInt(command) > currentUser.getMyMap().size() || Integer.parseInt(command) < 1)
            return "Invalid bounds!";
        gameMap = currentUser.getMyMap().get(Integer.parseInt(command) - 1);
        return "start";
    }

    public String showMap() {
        return null;
    }
    public String moveMap() {
        return null;
    }
    public String showMapDetails(Matcher matcher) {
        return null;
    }
    public String showPopularityFactors() {
        return null;
    }
    public String showPopularity() {
        return null;
    }
    public String showFoodList() {
        return null;
    }
    public String foodRate(Matcher matcher) {
        return null;
    }
    public String showFoodRate() {
        return null;
    }
    public String taxRate(Matcher matcher) {
        return null;
    }
    public String showTaxRate() {
       return null;
    }
    public String fearRate(Matcher matcher) {
        return null;
    }
    public String dropBuilding(Matcher matcher) {
        //TODO multiple command ....
        return null;
    }
    public String selectBuilding(Matcher matcher) {
        return null;
    }
    public String createUnit(Matcher matcher) {
        return null;
    }
    public String repair() {
        return null;
    }
    public String selectUnit(Matcher matcher) {
        return null;
    }
    public String moveUnit(Matcher matcher) {
        return null;
    }
    public String patrolUnit(Matcher matcher) {
        return null;
    }
    public String setUnit(Matcher matcher) {
        return null;
    }
    public String attack(Matcher matcher) {
        return null;
    }
    public String pourOil(Matcher matcher) {
        return null;
    }
    public String digTunnel(Matcher matcher) {
        return null;
    }
    public String build(Matcher matcher) {
        return null;
    }
    public String disbandUnit() {
        return null;
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
    public String dropUnit(Matcher matcher) {
        return null;
    }

}
