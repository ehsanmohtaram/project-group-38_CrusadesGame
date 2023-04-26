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
        User.users.addAll(Database.setArrayOfUsers());
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
                case "logout":
                    if (loginController.run().equals("exit"))
                        return;
                    break;
            }
        }
    }

    public String showDefaultMaps(){
        Map.createDefaultMaps();
        String result = "";
        for (Map defaults: Map.DEFAULT_MAPS) {
            result += defaults.getMapName() + ": " + defaults.getMapWidth() + " * " + defaults.getMapHeight() + '\n';
        }
        return result;
    }

    public String selectDefaultMap(String selectedIndex){
        try{
            int index = Integer.parseInt(selectedIndex);
            gameMap = Map.getDefaultMap(index - 1);
            if(gameMap == null)
                return "invalid number";
            return "successful";
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
        if(width < 0 || height < 0)
            return "invalid bounds";
        gameMap = new Map(width, height, options.get("n"));
        return "successful";
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
