package controller;

import model.ResourceType;
import model.Trade;
import model.User;
import view.*;

import java.util.HashMap;
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

    public Controller() {
        this.gameMenu = new GameMenu(this);
        this.loginMenu = new LoginMenu(this);
        this.profileMenu = new ProfileMenu(this);
        this.shopMenu = new ShopMenu(this);
        this.signupMenu = new SignupMenu(this);
        this.unitMenu = new UnitMenu(this);
        this.buildingMenu = new BuildingMenu(this);
        this.mainMenu = new MainMenu(this);
        this.selectMapMenu = new SelectMapMenu(this);
        this.tradeMenu = new TradeMenu(this);
    }
    public void run() {
//        if (loginMenu.run().equals("exit"))
//            return;
        while (true) {
            switch (mainMenu.run()) {
                case "trade":
                    showNotification();
                    tradeMenu.run();
                case "shop":
                    shopMenu.run();
                    break;
                case "selectMap":
                    selectMapMenu.run();
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
    public static String createUser(HashMap<String, String> options) {
        for (String optionMap : options.keySet()) {
            System.out.println(optionMap + " " + options.get(optionMap));
        }
        return "hello";
    }
    private String securityQuestion(Matcher matcher) {
        return null;
    }
    public String login(Matcher matcher) {
        return null;
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
