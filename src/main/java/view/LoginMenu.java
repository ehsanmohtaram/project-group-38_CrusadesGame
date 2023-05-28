package view;

import controller.CommandParser;
import controller.LoginController;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LoginMenu extends Application {

    public static Stage stage;
    @Override
    public void start(Stage stage) {
        LoginMenu.stage = stage;
        stage.setResizable(false);
        Pane pane = new Pane();
        Scene scene = new Scene(pane, 1280, 720);
        stage.setScene(scene);
        stage.setTitle("Login Menu");
        stage.show();
    }

    public void loginInfo(Pane pane) {
        VBox vBox = new VBox();


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
