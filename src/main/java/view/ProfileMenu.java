package view;

import controller.CommandParser;
//import controller.ProfileController;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.*;
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
    private Pane mainPane;
    private String name = "ehsan";
    public ProfileMenu() {
        this.style = new Style();
//        this.profileController = profileController;
//        commandParser = new CommandParser();
    }


    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        stage.setResizable(false);
        Pane pane = new Pane();
        this.mainPane = pane;
        pane.setBackground(Background.fill(Color.YELLOW));
        profileView(pane);
        Scene scene = new Scene(pane, 1280, 720);
        stage.setScene(scene);
        stage.setTitle("Login Menu");
        stage.show();
    }

    private void profileView(Pane pane) {
        Pane profilePane = new Pane();
        Label profile = new Label("Profile");
        profile.setFont(style.Font0(40));
        profile.setLayoutX(580);profile.setLayoutY(47);
        profile.setTextFill(Color.BLACK);
        profilePane.getChildren().add(profile);
        pane.getChildren().add(profilePane);
        Circle circle = new Circle(640, 1000, 800);
        circle.setFill(Color.LIGHTGOLDENRODYELLOW);
        profilePane.getChildren().add(circle);
        Circle avatar = new Circle(640, 200, 70);
        avatar.setFill(Color.WHITE);
        profilePane.getChildren().add(avatar);
        Rectangle rectangle = new Rectangle(400, 270);
        rectangle.setArcWidth(30);rectangle.setArcHeight(30);
        rectangle.setX(455);rectangle.setY(320);
        rectangle.setFill(Color.WHITE);
        rectangle.setStyle("-fx-background-radius: 20");
        profilePane.getChildren().add(rectangle);
        createText("My Details",450, 310, 15, Color.GRAY, profilePane);
        createText("Username:", 470, 350, 15, Color.GRAY, profilePane);
        Text username = createText(name, 475, 380, 20, Color.BLACK, profilePane);
        createText("Password:",470, 410, 15, Color.GRAY, profilePane);
        createText("ehsan@1383",475, 440, 20, Color.BLACK, profilePane);
        createText("Nick Name:",470, 470, 15, Color.GRAY, profilePane);
        createText("ehsan khalaf",475, 500, 20, Color.BLACK, profilePane);
        createText("Email Address:",470, 530, 15, Color.GRAY, profilePane);
        createText("ehsanmohtaram@gmail.com",475, 560, 20, Color.BLACK, profilePane);




        Button editName = createButton("edit",800, 330,20,50, 15, profilePane);
        editName.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                System.out.println("lknasdkjn");
                editName(username, profilePane);
            }
        });
        Button scoreBord = createButton("ScoreBord", 560, 600, 80, 200, 30, profilePane);
        scoreBord.setTextFill(Color.BLACK);
        scoreBord.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                scoreBord(profilePane);
            }
        });
    }

    public Text createText(String containText,int x, int y, int font, Color color, Pane pane) {
        Text text = new Text(x,y, containText);
        text.setFont(style.Font0(font));
        text.setFill(color);
        pane.getChildren().add(text);
        return text;
    }

    public Button createButton(String containText, int x, int y, int height, int width, int fontSize, Pane pane){
        Button button = new Button();
        button.setLayoutX(x);button.setLayoutY(y);
        style.button0(button, containText,width, height);
        button.setFont(style.Font0(fontSize));
        pane.getChildren().add(button);
        return button;
    }

    private void scoreBord(Pane profilePane) {
        StackPane scoreBord = new StackPane();
        profilePane.getChildren().add(scoreBord);
        Rectangle rectangle = new Rectangle(100, 100);
        rectangle.setLayoutX(300);
        rectangle.setFill(Color.BLACK);
        scoreBord.getChildren().add(rectangle);
    }

    private void editName(Text username, Pane profilePane){
        TextField editName = new TextField();
        editName.setPromptText(username.getText());
        editName.setFont(style.Font0(20));
        editName.setBackground(Background.fill(Color.WHITE));
        editName.setLayoutX(462);editName.setLayoutY(351.5);
        profilePane.getChildren().add(editName);
        profilePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                name = editName.getText();
                profileView(mainPane);
            }
        });
        profilePane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                name = editName.getText();
                profileView(mainPane);
            }
        });

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
