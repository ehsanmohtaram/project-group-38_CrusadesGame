package view;

import controller.CommandParser;
//import controller.ProfileController;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;

public class ProfileMenu extends Application {
//    private final ProfileController profileController;
//    private final CommandParser commandParser;
    public Stage stage;
    private Style style;
    public ProfileMenu() {
        this.style = new Style();
//        this.profileController = profileController;
//        commandParser = new CommandParser();
    }


    @Override
    public void start(Stage stage) throws Exception {
        LoginMenu.stage = stage;
        stage.setResizable(false);
        Pane pane = new Pane();
        pane.setBackground(Background.fill(Color.YELLOW));
        ProfileView(pane);
        Scene scene = new Scene(pane, 500, 720);
        stage.setScene(scene);
        stage.setTitle("Login Menu");
        stage.show();
    }

    private void ProfileView(Pane pane) {
        Pane profilePane = new Pane();
        Label profile = new Label("Profile");
        profile.setFont(style.Font0(35));
        profile.setLayoutX(195);profile.setLayoutY(47);
        profile.setTextFill(Color.BLACK);
        profilePane.getChildren().add(profile);
        pane.getChildren().add(profilePane);
        Circle circle = new Circle(250, 700, 500);
        circle.setFill(Color.LIGHTGOLDENRODYELLOW);
        profilePane.getChildren().add(circle);
        Circle avatar = new Circle(250, 200, 63);
        avatar.setFill(Color.WHITE);
        profilePane.getChildren().add(avatar);
        createText("My Details",50, 310, 15, Color.GRAY, profilePane);
        Rectangle rectangle = new Rectangle(400, 270);
        rectangle.setArcWidth(30);
        rectangle.setArcHeight(30);
        rectangle.setX(55);rectangle.setY(320);
        rectangle.setFill(Color.WHITE);
        rectangle.setStyle("-fx-background-radius: 20");
        profilePane.getChildren().add(rectangle);
        createText("Username:", 65, 350, 15, Color.GRAY, profilePane);
        createText("ehsan", 70, 380, 20, Color.BLACK, profilePane);
        createText("Password:",65, 410, 15, Color.GRAY, profilePane);
        createText("ehsan@1383",70, 440, 20, Color.BLACK, profilePane);
        createText("Nick Name:",65, 470, 15, Color.GRAY, profilePane);
        createText("ehsan khalaf",70, 500, 20, Color.BLACK, profilePane);
        createText("Email Address:",65, 530, 15, Color.GRAY, profilePane);
        createText("ehsanmohtaram@gamail.com",70, 560, 20, Color.BLACK, profilePane);

    }

    public void createText(String containText,int x, int y, int font, Color color, Pane pane) {
        Text text = new Text(x,y, containText);
        text.setFont(style.Font0(font));
        text.setFill(color);
        pane.getChildren().add(text);
    }

    public String run() {
//        HashMap<String, String> optionPass;
//        String command;
//        while (true) {
//            command = CommandParser.getScanner().nextLine();
//            if (commandParser.validate(command,"back",null) != null) return "back";
//            if (commandParser.validate(command,"show current menu",null) != null) System.out.println("Profile Menu");
//            else if ((optionPass = commandParser.validate(command,"profile change","u|username/n|nickname/e|email/s|slogan")) != null)
//                System.out.println(profileController.profileChange(optionPass));
//            else if ((optionPass = commandParser.validate(command,"profile change password","o|oldPassword/n|newPassword")) != null)
//                System.out.println(profileController.changePassword(optionPass));
//            else if (commandParser.validate(command,"profile remove slogan",null) != null)
//                System.out.println(profileController.removeSlogan());
//            else if (commandParser.validate(command,"profile display highscore",null) != null)
//                System.out.println(profileController.displayInfoSeparately(true,false,false));
//            else if (commandParser.validate(command,"profile display rank",null) != null)
//                System.out.println(profileController.displayInfoSeparately(false,true,false));
//            else if (commandParser.validate(command,"profile display slogan",null) != null)
//                System.out.println(profileController.displayInfoSeparately(false,false,true));
//            else if (commandParser.validate(command,"profile display",null) != null)
//                System.out.println(profileController.displayAllInfo());
//            else System.out.println("Invalid command!");
//        }
        return null;
    }
}
