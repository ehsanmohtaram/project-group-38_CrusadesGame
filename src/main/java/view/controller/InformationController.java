package view.controller;

import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.User;

public class InformationController {
    private TextField username;
    private TextField password;
    private TextField confirmation;
    private TextField email;
    private TextField nickname;
    private TextField securityAnswer;
    private String securityValue;
    private TextField slogan;

    public void updateTextYouWant(TextField textField, int type) {
        switch (type) {
            case 0 : this.username = textField; break;
            case 1 : this.password = textField; break;
            case 2 : this.confirmation = textField; break;
            case 3 : this.email = textField; break;
            case 4 : this.nickname = textField; break;
            case 5 : this.securityAnswer = textField; break;
        }
    }

    public String usernameError() {
        if (username.getText().matches(".*[^A-Za-z0-9_]+.*")) return "Incorrect format of username!";
        else if (User.getUserByUsername(username.getText()) != null) return "Username already exists!";
        else if (username.getText().length() == 0) return "Please fill mandatory filed!";
        else return null;
    }

    public String passwordError() {
        if (password.getText().length() < 6 || !password.getText().matches(".*[a-z]+.*") ||
                !password.getText().matches(".*[A-Z]+.*") || !password.getText().matches(".*[0-9]+.*") ||
                !password.getText().matches(".*\\W+.*")) return "Weak password. Please set a strong password!";
        else if (!password.getText().equals(confirmation.getText())) return "Confirmation did not match with password!";
        else return null;
    }

    public String confirmationError() {
        if (!password.getText().equals(confirmation.getText())) return "Confirmation did not match with password!";
        else return null;
    }

    public String emailError() {
        if (!email.getText().matches("^[A-Za-z0-9_]+@[A-Za-z0-9_]+\\.[A-Za-z0-9_]+$")) return "Incorrect format of email!";
        else if (User.checkForEmailDuplication(email.getText().toLowerCase())) return "Email already exists!";
        else return null;
    }

    public String nicknameError() {
        if (nickname.getText().length() == 0) return "Please fill mandatory filed!";
        else return null;
    }

    public String securityError(ChoiceBox<String> choiceBox) {
        if (securityAnswer.getText().length() == 0 || choiceBox.getValue() == null) return "Please fill mandatory filed!";
        else return null;

    }

    public void updateAllInfoTogether(TextField username, TextField nickName, TextField email, TextField password, TextField slogan,TextField securityAnswer, String securityValue) {
        this.username = username;
        this.nickname = nickName;
        this.email = email;
        this.password = password;
        this.securityAnswer = securityAnswer;
        this.slogan = slogan;
        this.securityValue = securityValue;
        if (username.getText().equals("")) {username.setText("h"); username.setText("");}
        if (nickName.getText().equals("")) {nickName.setText("h"); nickName.setText("");}
        if (email.getText().equals("")) {email.setText("h"); email.setText("");}
        if (password.getText().equals("")) {password.setText("h"); password.setText("");}
        if (securityAnswer.getText().equals("")) {securityAnswer.setText("h"); securityAnswer.setText("");}
        if (slogan.getText().equals("")) {slogan.setText("h"); slogan.setText("");}
        if (securityValue == null) {securityAnswer.setText("h"); securityAnswer.setText("");}
    }

}
