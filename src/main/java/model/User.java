package model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.internal.bind.DateTypeAdapter;
import org.apache.commons.codec.digest.DigestUtils;
import view.TradeMenu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class User {
    public static ArrayList<User> users = new ArrayList<>();
    public static HashMap<User, Integer> loginDelays = new HashMap<>();
    public static HashMap<User, Boolean>  isDelayed= new HashMap<>();
    public static HashMap<User, Long> whenDelayStarted = new HashMap<>();
    public final static ArrayList<String> questions = new ArrayList<>(Arrays.asList(
            "What is my father’s name?",
            "Was my first pet’s name?",
            "What is my mother’s last name?"
    ));
    @Expose
    private String userName;
    @Expose
    private String password;
    @Expose
    private String email;
    @Expose
    private String slogan;
    @Expose
    private String nickName;
    @Expose
    private Boolean isLoggedIn;
    @Expose
    private Integer score;
    @Expose
    private Integer securityQuestionNumber;
    @Expose
    private String answerToSecurityQuestion;
    @Expose
    private ArrayList<Trade> myRequests = new ArrayList<>();
    @Expose
    private ArrayList<Trade> mySuggestion = new ArrayList<>();
    @Expose
    private ArrayList<Trade> notification = new ArrayList<>();
    @Expose
    private ArrayList<Trade> historyTrade = new ArrayList<>();
    private ArrayList<Map> myMap = new ArrayList<>();
    private ArrayList<Kingdom> kingdom = new ArrayList<>();
    public User(String userName, String nickName,String password, String email, String slogan, Integer securityQuestionNumber, String answerToSecurityQuestion) {
        this.userName = userName;
        this.nickName = nickName;
        this.password = password;
        this.email = email;
        if (slogan == null) slogan = "";
        this.slogan = slogan;
        this.securityQuestionNumber = securityQuestionNumber;
        this.answerToSecurityQuestion = answerToSecurityQuestion;
        isLoggedIn = false;
        score = 0;
        users.add(this);
        loginDelays.put(this,-15);
        isDelayed.put(this,false);
    }

    public static void addUser(String username, String nickname, String password, String email, String slogan, Integer securityQuestionNumber, String answerToSecurityQuestion) {
        new User(username,nickname,password,email,slogan,securityQuestionNumber,answerToSecurityQuestion);
    }

    public static User getUserByUsername (String userName) {
        for (User user : users) if (user.getUserName().equals(userName)) return user;
        return null;
    }

    public static void setDelayForUser(String userName) {
        User user = getUserByUsername(userName);
        if (loginDelays.containsKey(user)) loginDelays.put(user,loginDelays.get(user) + 5);
    }

    public static Integer getTimeOfDelayOfUser(String userName) {
        User user = getUserByUsername(userName);
        return loginDelays.get(user);
    }

    public static void setDelayStatue(String userName) {
        User user = getUserByUsername(userName);
        if (isDelayed.get(user).equals(false)) isDelayed.put(user,true);
        else isDelayed.put(user,false);
    }

    public static Boolean getStatueOfDelayOfUser(String userName) {
        User user = getUserByUsername(userName);
        return isDelayed.get(user);
    }

    public static void setStartOfDelay(String userName) {
        User user = getUserByUsername(userName);
        whenDelayStarted.put(user,System.currentTimeMillis());
    }

    public static Long getStartOfDelay(String userName) {
        User user = getUserByUsername(userName);
        return whenDelayStarted.get(user);
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ArrayList<Trade> getMyRequests() {
        return myRequests;
    }

    public ArrayList<Trade> getHistoryTrade() {
        return historyTrade;
    }

    public ArrayList<Trade> getMySuggestion() {
        return mySuggestion;
    }

    public boolean checkPassword(String password) {
        return this.password.equals(DigestUtils.sha256Hex(password));
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getScore() {
        return score;
    }



    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public ArrayList<Trade> getNotification() {
        return notification;
    }



    public static Boolean checkForEmailDuplication(String email) {
        for (User user : users) if (user.getEmail().toLowerCase().equals(email)) return true;
        return false;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSlogan() {
        return slogan;
    }

    public void setSlogan(String slogan) {
        this.slogan = slogan;
    }

    public Boolean getLoggedIn() {
        return isLoggedIn;
    }

    public void setLoggedIn(Boolean loggedIn) {
        isLoggedIn = loggedIn;
    }

    public Integer getSecurityQuestionNumber() {
        return securityQuestionNumber;
    }

    public String getAnswerToSecurityQuestion() {
        return answerToSecurityQuestion;
    }

    public void addToMyMap(Map myNewMap) {
        this.myMap.add(myNewMap);
    }
}
