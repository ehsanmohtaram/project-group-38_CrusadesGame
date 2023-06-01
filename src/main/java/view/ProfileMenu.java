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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Objects;

public class ProfileMenu extends Application {
//    private final ProfileController profileController;
//    private final CommandParser commandParser;
    public Stage stage;
    private Style style;
    private Pane mainPane;
    private String name = "ehsan";
    private String password = "ehsan1383";
    private String nickName = "gordon";
    private String email = "ehsan@gamil.com";
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
        BackgroundSize backgroundSize = new BackgroundSize(1920, 1080, false, false, false, false);
        Image image = new Image(Objects.requireNonNull(LoginMenu.class.getResource("/images/background/loginBack.jpg")).toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        pane.setBackground(new Background(backgroundImage));
        profileView(pane);
        stage.getScene().setRoot(pane);
        stage.setTitle("Login Menu");
        stage.show();
    }

    public void profileView(Pane pane) {
        System.out.println("start");
        Pane profilePane = new Pane();
        profilePane.setPrefSize(550,700);
        profilePane.setLayoutX(850);profilePane.setLayoutY(122);
        profilePane.setBorder(new Border(new BorderStroke(Color.WHITE, BorderStrokeStyle.SOLID, new CornerRadii(10), BorderStroke.THIN)));
        pane.getChildren().add(profilePane);
        Label profileLabel = new Label("Profile");
        profileLabel.setFont(style.Font0(40));
        profileLabel.setLayoutX(180);profileLabel.setLayoutY(10);
        profileLabel.setTextFill(Color.BLACK);
        profilePane.getChildren().add(profileLabel);
        Circle avatar = new Circle(250, 150, 70);
        avatar.setFill(Color.WHITE);
        profilePane.getChildren().add(avatar);
        createText("Username:", 45, 260, 20, Color.BLACK, profilePane);
        TextField usernameField = createTextField(name, 75, 275, 45, 390, profilePane);
        createText("Password:",45, 350, 20, Color.BLACK, profilePane);
        TextField passwordField = createTextField(password,75, 365, 45, 390, profilePane);
        createText("Nick Name:",45, 440, 20, Color.BLACK, profilePane);
        TextField nickNameField = createTextField(nickName,75, 455, 45, 390, profilePane);
        createText("Email Address:",45, 530, 20, Color.BLACK, profilePane);
        TextField emailField = createTextField(email,75, 545, 45, 390, profilePane);
//        Rectangle rectangle = new Rectangle();
//        rectangle.setLayoutX(430);rectangle.setLayoutY(285);
//        rectangle.setHeight(25);rectangle.setWidth(25);
//        rectangle.setVisible(false);
//        profilePane.getChildren().add(rectangle);
        ImageView editImageForName = createEdit(430, 283, profilePane);
        ImageView editImageForPassword = createEdit(430, 373, profilePane);
        ImageView editImageForNickName = createEdit(430, 463, profilePane);
        ImageView editImageForEmail = createEdit(430, 553, profilePane);
        Button scoreBord = createButton("ScoreBord", 170, 610, 60, 200, 30, profilePane);
        scoreBord.setTextFill(Color.BLACK);
        editImageForName.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                editName(usernameField, profilePane);
            }
        });
        editImageForPassword.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                editPassword(passwordField, profilePane);
            }
        });
        editImageForNickName.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                editNickName(nickNameField, profilePane);
            }
        });
        editImageForEmail.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                editEmail(emailField, profilePane);
            }
        });
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

    public TextField createTextField(String containText, int x, int y, int height, int width, Pane pane){
        TextField textField = new TextField();
        textField.setLayoutX(x);textField.setLayoutY(y);
        style.textFiled0(textField, containText,width, height);
//        textField.setStyle("-fx-prompt-text-fill: black; -fx-text-fill: black");
        textField.setFont(style.Font0(20));
        pane.getChildren().add(textField);
        textField.setStyle("-fx-text-fill-disabled: #999999");
        textField.setDisable(true);
        return textField;
    }

    public Button createButton(String containText, int x, int y, int height, int width, int fontSize, Pane pane){
        Button button = new Button();
        button.setLayoutX(x);button.setLayoutY(y);
        style.button0(button, containText,width, height);
        button.setFont(style.Font0(fontSize));
        pane.getChildren().add(button);
        return button;
    }

    public ImageView createEdit(int x, int y, Pane pane){
        ImageView imageView = new ImageView(new Image(ProfileMenu.class.getResource("/images/buttons/edit.png").toExternalForm()));
        imageView.setLayoutX(x);imageView.setLayoutY(y);
        imageView.setFitHeight(25);imageView.setFitWidth(25);
        pane.getChildren().add(imageView);
        return imageView;
    }

    private void scoreBord(Pane profilePane) {
        StackPane scoreBord = new StackPane();
        profilePane.getChildren().add(scoreBord);
        Rectangle rectangle = new Rectangle(100, 100);
        rectangle.setLayoutX(300);
        rectangle.setFill(Color.BLACK);
        scoreBord.getChildren().add(rectangle);
    }

    private void editName(TextField textField, Pane profilePane){
        textField.setDisable(false);
        profilePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                name = textField.getText();
                textField.setText("");
                textField.setPromptText("");
                profileView(mainPane);
            }
        });
        profilePane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                name = textField.getText();
                textField.setText("");
                textField.setPromptText("");
                profileView(mainPane);
            }
        });
    }

    private void editPassword(TextField textField, Pane profilePane){
        textField.setDisable(false);
        profilePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                password = textField.getText();
                textField.setText("");
                textField.setPromptText("");
                profileView(mainPane);
            }
        });
        profilePane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                password = textField.getText();
                textField.setText("");
                textField.setPromptText("");
                profileView(mainPane);
            }
        });
    }

    private void editNickName(TextField textField, Pane profilePane){
        textField.setDisable(false);
        profilePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                nickName = textField.getText();
                textField.setText("");
                textField.setPromptText("");
                profileView(mainPane);
            }
        });
        profilePane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                nickName = textField.getText();
                textField.setText("");
                textField.setPromptText("");
                profileView(mainPane);
            }
        });
    }

    private void editEmail(TextField textField, Pane profilePane){
        textField.setDisable(false);
        profilePane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                email = textField.getText();
                textField.setText("");
                textField.setPromptText("");
                profileView(mainPane);
            }
        });
        profilePane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                email = textField.getText();
                textField.setText("");
                textField.setPromptText("");
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
