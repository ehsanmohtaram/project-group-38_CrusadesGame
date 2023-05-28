package view;

import controller.CommandParser;
import controller.LoginController;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import model.Database;

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
        pane.setBackground(Background.fill(Color.CADETBLUE));
        loginInfo(pane);
        Scene scene = new Scene(pane, 1280, 720);
        stage.setScene(scene);
        stage.setTitle("Login Menu");
        stage.show();
    }

    public void loginInfo(Pane pane) {
        VBox vBox = new VBox();
        vBox.setSpacing(30);
        vBox.setAlignment(Pos.CENTER);
        Label singIn = new Label("Sign in to the game");
        singIn.setFont(style.Font0(40));
        singIn.setTextFill(Color.WHITE);
        TextField userName = new TextField();
        userName.setFont(style.Font0(20));
        userName.setStyle("-fx-text-fill: white;");
        TextField password = new TextField();
        password.setFont(style.Font0(20));
        password.setStyle("-fx-text-fill: white;");
        style.textFiled0(userName,"Username" ,300, 60);
        style.textFiled0(password,"Password" ,300, 60);
        Button login = new Button();
        style.button0(login, "Login", 300, 60);
        vBox.getChildren().addAll(singIn, userName, password, login);
        vBox.setLayoutX(600);  vBox.setLayoutY(100);
        vBox.setPrefSize(600, 600);
        vBox.setBackground(Background.fill(Color.GRAY));
        pane.getChildren().add(vBox);
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
