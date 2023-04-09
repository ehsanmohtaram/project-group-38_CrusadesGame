package model;

import java.util.ArrayList;

public class User {
    public static ArrayList<User> USERS = new ArrayList<>();
    private String userName;
    private String password;
    private String email;
    private String slogan;
    private String nickName;
    private Integer score;
    private ArrayList<Map> myMap = new ArrayList<>();
    private Kingdom kingdom;
    private ArrayList<String> answerToSecurityQuestion = new ArrayList<>(3);
    public User(String userName, String password, String email, String slogan) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.slogan = slogan;
        score = 0;
        USERS.add(this);
    }
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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


}
