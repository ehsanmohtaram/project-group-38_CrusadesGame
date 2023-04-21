package model;

import java.util.ArrayList;

public class User {
    public static ArrayList<User> users = new ArrayList<>();
    public static ArrayList<String> emails = new ArrayList<>();
    private String userName;
    private String password;
    private String email;
    private String slogan;
    private String nickName;
    private Integer score;
    private ArrayList<Trade> myRequests = new ArrayList<>();
    private ArrayList<Trade> mySuggestion = new ArrayList<>();
    private ArrayList<Map> myMap = new ArrayList<>();
    private ArrayList<Kingdom> kingdom = new ArrayList<>();
    private ArrayList<String> answerToSecurityQuestion = new ArrayList<>(3);
    public User(String userName, String password, String email, String slogan) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.slogan = slogan;
        score = 0;
        users.add(this);
    }

    public static void addUser(String userName, String password, String email, String slogan){
        User user = new User(userName, password, email, slogan);
        users.add(user);
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

    public ArrayList<Trade> getMyRequests() {
        return myRequests;
    }

    public ArrayList<Trade> getMySuggestion() {
        return mySuggestion;
    }

    public String getPassword() {
        return password;
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
        for (User user : users) if (user.getEmail().equals(email)) return true;
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


}
