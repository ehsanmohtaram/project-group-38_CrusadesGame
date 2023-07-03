package view.controller;
import controller.Connection;
import controller.LoginController;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.MainMenu;
import view.Style;

import java.util.HashMap;

public class LoginMenuController {
    private final LoginController loginController;
    private final Style style;
    private TextField password;
    private TextField username;
    private TextField captcha;
    private Rectangle captchaImage;
    private Rectangle stayLogin;
    private Stage stage;
    private TextField answer;

    public LoginMenuController() {
        loginController = new LoginController();
        style = new Style();
    }
    public void getInfoFromMenu(Stage stage, TextField password, TextField username, TextField captcha, Rectangle captchaImage, Rectangle stayLogin, TextField answer) {
        this.stage = stage;
        this.password = password;
        this.username = username;
        this.captcha = captcha;
        this.captchaImage = captchaImage;
        this.stayLogin = stayLogin;
        this.answer = answer;
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
        if (username.isDisable()) valueMaker.put("A", "*");
        else valueMaker.put("A", "-");
        valueMaker.put("a", answer.getText());
        result = loginController.login(valueMaker);
        if (result.equals("login")) {
            new MainMenu().start(stage);
            new Connection().startNewConnection();
        }
        else makeLoginAlert(result);
    }

    public void makeLoginAlert(String result) {
        VBox popUp = new VBox();
        Button button = new Button();
        Pane pane = ((Pane)username.getScene().getRoot());
        style.popUp0(pane, popUp, button, 80, 50, 400, 295, 400, 100,200, 50, 450, 213, result, 20);
        popUpTransition(popUp, 0, 1);
        pane.getChildren().get(0).setDisable(true);
        disableHBoxes(0.4);
        button.setOnMouseClicked(mouseEvent -> {
            popUpTransition(popUp, 1, 0);
        });
    }
    public void popUpTransition(VBox popUp,int in, int out) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.seconds(0.5));
        scaleTransition.setNode(popUp);
        scaleTransition.setFromX(in);
        scaleTransition.setToX(out);
        scaleTransition.setFromY(in);
        scaleTransition.setToY(out);
        scaleTransition.setCycleCount(1);
        scaleTransition.play();
        if (out == 0) {
            scaleTransition.setOnFinished(actionEvent -> {
                ((Pane) popUp.getParent()).getChildren().get(0).setDisable(false);
                disableHBoxes(1);
                ((Pane) popUp.getParent()).getChildren().remove(1);
            });
        }
    }

    public void disableHBoxes(double opacity) {
        ((HBox)(password.getParent())).setBorder(new Border(new BorderStroke(Color.rgb(86,73,57,opacity), BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        ((HBox)(password.getParent())).getChildren().get(1).setOpacity(opacity);
        captchaImage.getParent().setOpacity(opacity);
        if (opacity != 1) captchaImage.setOpacity(0);
        else captchaImage.setOpacity(1);
        stayLogin.setOpacity(opacity);
    }



}
