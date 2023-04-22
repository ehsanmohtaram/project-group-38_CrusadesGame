package controller;

import model.Database;
import model.User;
import org.json.simple.parser.ParseException;
import view.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.regex.Matcher;

public class Controller {
    private GameMenu gameMenu;
    private LoginMenu loginMenu;
    private ProfileMenu profileMenu;
    private ShopMenu shopMenu;
    private MainMenu mainMenu;
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

        try {
            FileWriter file = new FileWriter("src/main/resources/info.json");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void run() {
        if (loginMenu.run().equals("exit"))
            return;
        while (true) {
            switch (mainMenu.run()) {
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
        for (String key : options.keySet())
            if (!key.equals("s") && options.get(key) == null) return "Please input necessary options!";
        for (String key : options.keySet())
            if (options.get(key) != null && options.get(key).equals("")) return "Illegal value. Please fill the options!";
        if (options.get("u").matches(".*[^A-Za-z0-9_]+.*")) return "Incorrect format of username!";
        if (User.getUserByUsername(options.get("u")) != null) return "Username already exists!";
        if (!options.get("p").equals("random") && options.get("P") == null) return "Please fill password confirmation!";
        if (!options.get("p").equals("random") &&
                (options.get("p").length() < 6 ||
                !options.get("p").matches(".*[a-z]+.*") ||
                !options.get("p").matches(".*[A-Z]+.*") ||
                !options.get("p").matches(".*[0-9]+.*") ||
                !options.get("p").matches(".*\\W+.*"))) return "Weak password. Please set a strong password!";
        if (!options.get("p").equals("random") && !options.get("p").equals(options.get("P")))
            return "Password confirmation did not match the password!";
        if (!options.get("e").matches("^[A-Za-z0-9_]+@[A-Za-z0-9_]+\\.[A-Za-z0-9_]+$")) return "Incorrect format of email!";
        if (User.checkForEmailDuplication(options.get("e").toLowerCase())) return "Email already exists!";
        Database.addUser(options.get("u"),options.get("p"),options.get("n"),options.get("e"),options.get("s"));
        return "User created successfully!";
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
    public String showTradeList() {
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
