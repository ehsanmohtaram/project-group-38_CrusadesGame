package view.controller;
import controller.LoginController;
import javafx.scene.Node;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.HashMap;

public class LoginMenuController {
    private final LoginController loginController;
    private TextField password;
    private TextField username;
    private TextField captcha;
    private Rectangle captchaImage;
    private Rectangle stayLogin;

    public LoginMenuController() {
        loginController = new LoginController();
    }
    public void getInfoFromMenu(TextField password, TextField username, TextField captcha, Rectangle captchaImage, Rectangle stayLogin) {
        this.password = password;
        this.username = username;
        this.captcha = captcha;
        this.captchaImage = captchaImage;
        this.stayLogin = stayLogin;
    }

    public void checkLoginValidation() {
        String result;
        HashMap<String, String> valueMaker = new HashMap<>();
        valueMaker.put("u", username.getText());
        valueMaker.put("p", password.getText());
        if (stayLogin.getFill() instanceof ImagePattern) valueMaker.put("s", "done");
        else valueMaker.put("s", null);
        ImagePattern pattern = (ImagePattern) captchaImage.getFill();
        String imageName = pattern.getImage().getUrl().substring(pattern.getImage().getUrl().lastIndexOf("/") + 1);
        valueMaker.put("C", imageName);
        valueMaker.put("c", captcha.getText() + ".png");
        result = loginController.login(valueMaker);
        makeLoginAlert(result);
    }

    public void makeLoginAlert(String result) {
        Rectangle popUp = new Rectangle();
        popUp.setFill(Color.rgb(86,73,57,0.3));
        popUp.setArcHeight(20);
        popUp.setArcWidth(20);
        popUp.setWidth(600);
        popUp.setHeight(400);
        popUp.setLayoutX(456);
        popUp.setLayoutY(257);
        Pane pane = ((Pane)username.getScene().getRoot());
        pane.getChildren().add(popUp);
        pane.getChildren().get(0).setDisable(true);
        for (Node node : ((VBox) pane.getChildren().get(0)).getChildren())
            node.setDisable(true);
    }





}
