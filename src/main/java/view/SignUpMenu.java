package view;

import javafx.application.Application;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class SignUpMenu extends Application {
    @Override
    public void start(Stage stage) {
        stage.setResizable(false);
        Pane pane = new Pane();
        BackgroundSize backgroundSize = new BackgroundSize(1920, 1080, false, false, false, false);
        Image image = new Image(SignUpMenu.class.getResource("/images/background/loginBack.jpg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        pane.setBackground(new Background(backgroundImage));
        stage.getScene().setRoot(pane);
        stage.setTitle("Signup Menu");
        stage.setFullScreen(true);
        stage.show();
    }
}
