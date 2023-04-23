package model;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class User {
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<String> emails = new ArrayList<>();
    public final static ArrayList<String> questions = new ArrayList<>(Arrays.asList(
            "What is my father’s name?",
            "was my first pet’s name?",
            "What is my mother’s last name?"
    ));
    private String userName;
    private String password;
    private String email;
    private String slogan;
    private String nickName;
    private Integer score;
    private ArrayList<Trade> myTrades = new ArrayList<>();
    private ArrayList<Map> myMap = new ArrayList<>();
    private ArrayList<Kingdom> kingdom = new ArrayList<>();
    private Integer securityQuestionNumber;
    private String answerToSecurityQuestion;
    public User(String userName, String nickName,String password, String email, String slogan, Integer securityQuestionNumber, String answerToSecurityQuestion) {
        this.userName = userName;
        this.nickName = nickName;
        this.password = password;
        this.email = email;
        if (slogan == null) slogan = "";
        this.slogan = slogan;
        this.securityQuestionNumber = securityQuestionNumber;
        this.answerToSecurityQuestion = answerToSecurityQuestion;
        score = 0;
        users.add(this);
    }

    public static User getUserByUsername (String userName) {
        for (User user : users) if (user.getUserName().equals(userName)) return user;
        return null;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }



    public boolean checkPassword(String password) {
        return this.password.equals(DigestUtils.sha256Hex(password));
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void changePassword(String newPassword, String oldPassword) {
        setPassword(newPassword);
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

    public void setAnswerForSecurityQuestion(Integer securityQuestionNumber, String answer) {

    }
    public boolean checkSecurityQuestionAnswer(String answer) {
        return true;
    }
    
    public Integer getRank(){
        return null;
    }

    public void addToMyMap(Map myNewMap) {
        this.myMap.add(myNewMap);
    }
}
