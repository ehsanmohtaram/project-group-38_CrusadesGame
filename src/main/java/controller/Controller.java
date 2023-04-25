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
    private SignupMenu signupMenu;

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
        String result = "";
        for (Map defaults: Map.DEFAULT_MAPS) {
            result += defaults.getMapName() + ": " + defaults.getMapWidth() + " * " + defaults.getMapHeight() + '\n';
        }
        return result;
    }

    public String selectDefaultMap(String selectedIndex){
        try{
            int index = Integer.parseInt(selectedIndex);
            gameMap = Map.getDefaultMap(index);
            if(gameMap == null)
                return "invalid number";
            return "successful";
        }
        catch (Exception IllegalArgumentException){
            return "invalid input, please select a number";
        }
    }

    public String createNewMap(HashMap<String, String> options){
        for (String key : options.keySet()) if (options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet()) if (options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (!options.get("x").matches("-?\\d+") || !options.get("y").matches("-?\\d+"))
            return "Please input digits as x and y value!";
        int width = Integer.parseInt(options.get("x"));
        int height = Integer.parseInt(options.get("y"));
        if(width < 0 || height < 0) return "invalid bounds";
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
    public String trade(Matcher matcher) {
        return null;
    }
    public String tradeAccept(Matcher matcher) {
        return null;
    }
    public String showTradeHistory(Matcher matcher) {
        return null;
    }
    public String showPriceList(Matcher matcher) {
         return null;
    }
    public String buy(Matcher matcher) {
        return null;
    }
    public String sell(Matcher matcher) {
        return null;
    }
}
