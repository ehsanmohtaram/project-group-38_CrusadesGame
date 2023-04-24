package controller;

import model.Database;
import model.User;
import model.*;
import org.apache.commons.codec.digest.DigestUtils;
import view.*;

import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;
import java.util.regex.Matcher;

public class Controller {
    private GameMenu gameMenu;
    private LoginMenu loginMenu;
    private ProfileMenu profileMenu;
    private ShopMenu shopMenu;
    private MainMenu mainMenu;
    private TradeMenu tradeMenu;
    private SelectMapMenu selectMapMenu;
    private SignupMenu signupMenu;
    private UnitMenu unitMenu;
    private BuildingMenu buildingMenu;
    public static User currentUser;
    public static boolean stayLoggedIn = false;
    private Map gameMap;

    public Controller() {
        this.gameMenu = new GameMenu(this);
        this.loginMenu = new LoginMenu(this);
        this.profileMenu = new ProfileMenu(this);
        this.shopMenu = new ShopMenu(this);
        this.signupMenu = new SignupMenu(this);
        this.unitMenu = new UnitMenu(this);
        this.buildingMenu = new BuildingMenu(this);
        this.mainMenu = new MainMenu(this);
        this.tradeMenu = new TradeMenu(this);
        User.users.addAll(Database.setArrayOfUsers());
    }

    public void run() {
        if (loginMenu.run().equals("exit"))
            return;
        while (true) {
            switch (mainMenu.run()) {
                case "trade":
                    showNotification();
                    tradeMenu.run();
                case "shop":
                    shopMenu.run();
                    break;
                case "selectMap":
//                  currentUser.addToMyMap();
                    MapDesignController mapDesignController = new MapDesignController(gameMap);
                    mapDesignController.run();
                    break;
                case "profile":
                    profileMenu.run();
                    break;
                case "logout":
                    if (loginMenu.run().equals("exit"))
                        return;
                    break;
            }
        }
    }
    public String createUser(HashMap<String, String> options) {
        String username = null; String password; String slogan;
        for (String key : options.keySet())
            if (!key.equals("s") && !key.equals("P") && options.get(key) == null) {
                System.out.println(key);return "Please input necessary options!";}
        for (String key : options.keySet())
            if (options.get(key) != null && options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (options.get("u").matches(".*[^A-Za-z0-9_]+.*")) return "Incorrect format of username!";
        if (!options.get("p").equals("random") && options.get("P") == null) return "Please fill password confirmation!";
        if (!options.get("p").equals("random") &&
                (options.get("p").length() < 6 || !options.get("p").matches(".*[a-z]+.*") ||
                !options.get("p").matches(".*[A-Z]+.*") || !options.get("p").matches(".*[0-9]+.*") ||
                !options.get("p").matches(".*\\W+.*"))) return "Weak password. Please set a strong password!";
        if (User.getUserByUsername(options.get("u")) != null) {
            System.out.println("Username already exists!");
            username = Randomize.randomUsername(options.get("u"));
            if (username == null) return "Please try again with new username!";
        }
        if (!options.get("p").equals("random") && !options.get("p").equals(options.get("P")))
            return "Password confirmation did not match the password!";
        if (options.get("p").equals("random")) {
            password = Randomize.randomPassword();
            if (password == null) return "Please try again with new password!";
        } else password = options.get("p");
        if (User.checkForEmailDuplication(options.get("e").toLowerCase())) return "Email already exists!";
        if (!options.get("e").matches("^[A-Za-z0-9_]+@[A-Za-z0-9_]+\\.[A-Za-z0-9_]+$")) return "Incorrect format of email!";
        if (username == null) username = options.get("u");
        if (options.get("s") != null && options.get("s").equals("random")) slogan = Randomize.randomSlogan();
        else slogan = options.get("s");
        password = DigestUtils.sha256Hex(password);
        return securityQuestion(username,password,options.get("n"),options.get("e"),slogan);
    }
    private String securityQuestion(String username, String password, String nikName, String email, String slogan) {
        System.out.println("Pick your security question: ");
        for (int i = 0; i < 3 ;i++) System.out.println((i + 1) + ". " + User.questions.get(i));
        CommandParser commandParser = new CommandParser();
        String inputCommand = CommandParser.getScanner().nextLine();
        HashMap<String, String> optionPass;
        optionPass = commandParser.validate(inputCommand,"question pick","q|question/a|answer/c|confirmation");
        if (optionPass == null) return "Invalid command!";
        for (String key : optionPass.keySet())
            if (optionPass.get(key) == null) return "Please input necessary options!";
        for (String key : optionPass.keySet())
            if (optionPass.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (!optionPass.get("q").matches("-?\\d+")) return "Illegal value. Please choose a digit.";
        if (Integer.parseInt(optionPass.get("q")) < 1 ||
                Integer.parseInt(optionPass.get("q")) > 3) return "Out of bound. Please choose a digit between 1 to 3.";
        if (!optionPass.get("c").equals(optionPass.get("a"))) return "Answer did not match with confirmation";
        User.addUser(username,nikName,password,email,slogan,Integer.parseInt(optionPass.get("q")) - 1,optionPass.get("a"));
        return "User has been added successfully!";
    }
    public String login(HashMap<String, String> options) {
        for (String key : options.keySet())
            if (!key.equals("s") && options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet())
            if (options.get(key) != null && !key.equals("s") && options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (User.getUserByUsername(options.get("u")) == null) return "Username and password did not match!";
        if (User.getStatueOfDelayOfUser(options.get("u")).equals(true))
            return "You can not login for now. Please try again later.";
        if (!User.getUserByUsername(options.get("u")).checkPassword(options.get("p"))) {
            User.setDelayForUser(options.get("u"));
            if (User.getTimeOfDelayOfUser(options.get("u")) > 0) {
                User.setDelayStatue(options.get("u"));
                Timer timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() { User.setDelayStatue(options.get("u")); timer.cancel();}
                }, User.getTimeOfDelayOfUser(options.get("u")) * 1000L);
            }
            return "Username and password did not match!";
        }
        if (options.get("s") != null) User.getUserByUsername(options.get("u")).setLoggedIn(true);
        currentUser = User.getUserByUsername(options.get("u"));
        return "login";
    }
    public String forgetPassword(Matcher matcher) {
        return null;
    }
    public String changeUsername(Matcher matcher) {
        return null;
    }
    public String changeNickname(Matcher matcher) {
        return null;
    }
    public String changeEmail(Matcher matcher) {
        return null;
    }
    public String changeSlogan(Matcher matcher) {
        return null;
    }
    public String changePassword(Matcher matcher) {
        return null;
    }
    public String displayHighScore() {
       return null;
    }
    public String displayRank() {
        return null;
    }
    public String displaySlogan() {
        return null;
    }
    public String displayInfo() {
        return null;
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

        if (options.get("x").equals("") || options.get("x") == null ||
            options.get("y").equals("") || options.get("y") == null) return "Please input width & height correctly ";
        if(options.get("n") == null || options.get("n").equals(""))
            return "you must choose a name for your map. use -n.";
        int width = Integer.parseInt(options.get("x"));
        int height = Integer.parseInt("y");
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
    public String newRequest(HashMap<String, String> options) {
        User userReceiver;
        for (String value : options.keySet()) {
            if (options.get(value) == null)
                return value + " option is not entered";
        }
        try {
            ResourceType resourceType = ResourceType.valueOf(options.get("t").toUpperCase());
            if ((userReceiver = User.getUserByUsername(options.get("u"))) != null) {
                int resourceAmount = Integer.parseInt(options.get("a"));
                int price = Integer.parseInt(options.get("p"));
                String massage = options.get("m");
                Trade trade = new Trade(resourceType, resourceAmount, price, currentUser, userReceiver, massage, Trade.countId);
                Trade.countId ++;
                Trade.getTrades().add(trade);
                currentUser.getMyRequests().add(trade);
                userReceiver.getMySuggestion().add(trade);
                userReceiver.getNotification().add(0, trade);
                return "The request was successfully registered";
            }
            else
                return "Username with this ID was not found";
        }
        catch (Exception IllegalArgumentException){
            return "This resource type does not exist";
        }
    }
    public String showTradeList() {
        String output = "your suggestions:";
        for (Trade trade : currentUser.getMySuggestion()) {
            output += "\nResource type: " + trade.getResourceType().name() + "resource amount: " + trade.getResourceAmount()
                    + "price: " + trade.getPrice() + "from: " + trade.getUserSender().getUserName()
                    + "id: " + trade.getId() + "massage: " + trade.getMassage();
        }
        currentUser.getNotification().clear();
        return output;
    }
    public String tradeAccept(HashMap<String, String> options) {
        for (String value : options.keySet()) {
            if (options.get(value) == null)
                return value + " option is not entered";
        }
        for (Trade trade : currentUser.getMySuggestion()) {
            if (trade.getId() == Integer.parseInt(options.get("i"))) {/////////////
             break;
            }
        }
        return "This ID was not found for you";
    }
    public String showTradeHistory() {
        String output = "your history:";
        for (Trade trade : currentUser.getHistoryTrade()) {
                output += "\nResource type: " + trade.getResourceType().name() + "resource amount: " + trade.getResourceAmount()
                        + "price: " + trade.getPrice()+ "id: " + trade.getId() + "massage: " + trade.getMassage();
            if (trade.getUserSender().equals(currentUser))
                output += " ...Requested...";
            else
                output += " ...Accepted...";
        }
        return output;
    }
    public String showNotification() {
        String output = "your new suggestions:";
        for (Trade trade : currentUser.getNotification()) {
            output += "\nnew suggestion form " + trade.getUserSender() + "massage: " + trade.getMassage();
        }
        return output;
    }
}
