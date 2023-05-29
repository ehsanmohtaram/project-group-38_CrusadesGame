package view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import model.Database;

import java.util.Objects;
import java.util.Random;

public class LoginMenu extends Application {

    private final Style style;
    public static Stage stage;

    public LoginMenu() {
        this.style = new Style();
    }

    public static void main(String[] args) {
        Database.setArrayOfUsers();
        launch(args);
        Database.updateJson();
    }

    @Override
    public void start(Stage stage) {
        LoginMenu.stage = stage;
        stage.setResizable(false);
        Pane pane = new Pane();
        BackgroundSize backgroundSize = new BackgroundSize(1280, 720, false, false, false, false);
        Image image = new Image(Objects.requireNonNull(LoginMenu.class.getResource("/images/background/06.jpg")).toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        pane.setBackground(new Background(backgroundImage));
        loginInfo(pane);
        Scene scene = new Scene(pane, 1280, 720);
        stage.setScene(scene);
        stage.setTitle("Login Menu");
        stage.show();
    }

    public void loginInfo(Pane pane) {
        VBox vBox = new VBox();
        vBox.setSpacing(20);
        vBox.setAlignment(Pos.CENTER);
        Label singIn = new Label("SIGN IN");
        singIn.setFont(style.Font0(25));
        singIn.setTextFill(Color.WHITE);
        TextField userName = new TextField();
        userName.setFont(style.Font0(20));
        TextField password = new TextField();
        password.setFont(style.Font0(20));
        style.textFiled0(userName,"Username" ,300, 60);
        style.textFiled0(password,"Password" ,300, 60);
        HBox hBox0 = new HBox();
        hBox0.setSpacing(7);
        hBox0.setAlignment(Pos.CENTER);
        Rectangle checkStayLogin = new Rectangle();
        style.checkBox0(checkStayLogin);
        Label stayLogin = new Label("Stay Login");
        stayLogin.setTextFill(Color.WHITE);
        stayLogin.setFont(style.Font0(15));
        HBox.setMargin(stayLogin, new Insets(0, 55, 0 ,0));
        Label forgotPassword = new Label("Forgot Password?");
        forgotPassword.setTextFill(Color.WHITE);
        forgotPassword.setFont(style.Font0(15));
        hBox0.getChildren().addAll(checkStayLogin, stayLogin, forgotPassword);

        Button login = new Button();
        VBox.setMargin(login, new Insets(40, 0, 0 ,0));
        style.button0(login, "LOGIN", 300, 60);
        login.setFont(style.Font0(20));
        HBox hBox1 = new HBox();
        hBox1.setAlignment(Pos.CENTER);
        hBox1.setSpacing(10);
        Label newToGame = new Label("New to Game?");
        newToGame.setTextFill(Color.WHITE);
        newToGame.setFont(style.Font0(15));
        Label signUpMenu = new Label("Create an account");
        signUpMenu.setTextFill(Color.WHITE);
        signUpMenu.setFont(style.Font0(15));
        hBox1.getChildren().addAll(newToGame, signUpMenu);
        vBox.getChildren().addAll(singIn, userName, password, hBox0,login, hBox1);
        vBox.setPrefSize(400, 500);
        vBox.setLayoutX(800);  vBox.setLayoutY(110);
        vBox.setStyle("-fx-background-radius: 10;");
        vBox.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        vBox.setPadding(new Insets(20,20,20,20));
        pane.getChildren().addAll(vBox);
    }

    public void makeCaptcha(VBox vBox) {
        Rectangle rectangle = new Rectangle();
        Random random = new Random();
        rectangle.setFill(new ImagePattern(new Image(LoginMenu.class.getResource("/images/captcha/" + (random.nextInt(8999) + 1000) + ".png").toExternalForm())));
        vBox.getChildren().add(rectangle);

    }



    public String run() {
//        HashMap<String, String> optionPass;
//        String command ,result;
//        while (true) {
//            command = CommandParser.getScanner().nextLine();
//            if (commandParser.validate(command,"exit",null) != null) return "exit";
//            if (commandParser.validate(command,"show current menu",null) != null) System.out.println("Login Menu");
//            else if ((optionPass = commandParser.validate(command,"user create","u|username/?p|password/n|nickname/e|email/s|slogan")) != null) {
//                result = loginController.createUser(optionPass);
//                if (result != null) System.out.println(result);
//            }
//            else if ((optionPass = commandParser.validate(command,"user login","u|username/p|password/s|stay-logged-in")) != null) {
//                result = loginController.login(optionPass);
//                if (result.equals("login")) {System.out.println("User logged in successfully!"); return "login";}
//                else System.out.println(result);
//            }
//            else if ((optionPass = commandParser.validate(command,"forgot my password","u|username")) != null)
//                System.out.println(loginController.forgetPassword(optionPass));
//            else System.out.println("Invalid command!");
//        }
        return null;
        }
}
