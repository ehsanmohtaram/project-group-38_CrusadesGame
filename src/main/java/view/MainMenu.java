package view;

import controller.Controller;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainMenu extends Application {
    private final Style style;
    private Stage stage;
    public MainMenu() {
        style = new Style();
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setFullScreen(true);
        stage.setResizable(false);
        Pane pane = new Pane();
        BackgroundSize backgroundSize = new BackgroundSize(1920, 1080, false, false, false, false);
        Image image = new Image(SignUpMenu.class.getResource("/images/background/loginBack.jpg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        pane.setBackground(new Background(backgroundImage));
        stage.getScene().setRoot(pane);
        mainInfo(pane);
        stage.setTitle("Main Menu");
        stage.show();
    }

    public void mainInfo(Pane pane) {
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(50);
        Button startGame = new Button();
        style.button0(startGame, "Start Game", 400, 80);
        startGame.setFont(style.Font0(25));
        Button profileMenu = new Button();
        style.button0(profileMenu, "Profile", 400, 80);
        profileMenu.setFont(style.Font0(25));
        Button chat = new Button();
        style.button0(chat, "Chat", 400, 80);
        chat.setFont(style.Font0(25));
        Button logout = new Button();
        style.button0(logout, "Logout", 400, 80);
        logout.setFont(style.Font0(25));
        vBox.getChildren().addAll(startGame, profileMenu, chat,logout);
        vBox.setLayoutX(890);  vBox.setLayoutY(213);
        pane.getChildren().add(vBox);
        mainMenuButtonHandle(vBox);

    }

    public void mainMenuButtonHandle(VBox vBox) {
        vBox.getChildren().get(0).setOnMouseClicked(mouseEvent -> {
            try {new DesignMapMenu().start(stage);}
            catch (Exception ignored) {}
        });
        vBox.getChildren().get(1).setOnMouseClicked(mouseEvent -> {
            try {new ProfileMenu().start(stage);}
            catch (Exception ignored) {}
        });
        vBox.getChildren().get(2).setOnMouseClicked(mouseEvent -> {
            try {new ChatMenu().start(stage);}
            catch (Exception ignored) {}
        });
        vBox.getChildren().get(3).setOnMouseClicked(mouseEvent -> {
            if (Controller.currentUser.getLoggedIn()) Controller.currentUser.setLoggedIn(false);
            Controller.currentUser = Controller.loggedInUser = null;
            try {new LoginMenu().start(stage);}
            catch (Exception ignored) {}
        });
    }

}
