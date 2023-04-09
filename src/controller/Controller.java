package controller;

import view.*;

import java.util.regex.Matcher;

public class Controller {
    private GameMenu gameMenu;
    private LoginMenu loginMenu;
    private ProfileMenu profileMenu;
    private ShopMenu shopMenu;
    private SignupMenu signupMenu;

    public Controller() {
        this.gameMenu = new GameMenu(this);
        this.loginMenu = new LoginMenu(this);
        this.profileMenu = new ProfileMenu(this);
        this.shopMenu = new ShopMenu(this);
        this.signupMenu = new SignupMenu(this);
    }
    public void run() {

    }
    public String createUser(Matcher matcher) {
        return null;
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
