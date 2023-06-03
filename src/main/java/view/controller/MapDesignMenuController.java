package view.controller;

import controller.Controller;
import controller.MapDesignController;
import javafx.animation.ScaleTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.Duration;
import view.LoginMenu;
import view.Style;

import java.util.HashMap;
import java.util.Objects;

public class MapDesignMenuController {
    private MapDesignController mapDesignController;
    private final Controller controller;
    private final Style style;
    private Stage stage;
    private Integer width;
    private Integer height;
    private Label name;
    private StackPane mapDesignPane;

    public MapDesignMenuController() {
        controller = new Controller();
//        this.mapDesignController = new MapDesignController();
        this.style = new Style();
    }

    public void getInfoFromMenu(Stage stage, Integer width, Integer height, Label name){
        this.stage = stage;
        this.width = width;
        this.height = height;
        this.name = name;
    }

    public void createNewMap(){
        HashMap<String, String> optionsMaker = new HashMap<>();
        optionsMaker.put("x", width.toString());
        optionsMaker.put("y", height.toString());
        optionsMaker.put("n", name.toString());
//        String result =
        mapDesignController = controller.createNewMap(optionsMaker);
//        if(result.equals("successful"))
        startDesignMap();
//        else showError(result);
    }

    private void showError(String result) {

        VBox popUp = new VBox();
        Button button = new Button();
        Pane pane = ((Pane)name.getScene().getRoot());
        style.popUp0(pane, popUp, button, 80, 50, 400, 295, 400, 100,200, 50, 450, 213, result, 20);
        popUpTransition(popUp, 0, 1);
        pane.getChildren().get(0).setDisable(true);
//        disableHBoxes(0.4);
        button.setOnMouseClicked(mouseEvent -> {
            popUpTransition(popUp, 1, 0);
        });
    }

    private void startDesignMap() {
        mapDesignPane = new StackPane();
        BackgroundSize backgroundSize = new BackgroundSize(1920, 1080, false, false, false, false);
        Image image = new Image(Objects.requireNonNull(LoginMenu.class.getResource("/images/background/designMenu.jpg")).toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        mapDesignPane.setBackground(new Background(backgroundImage));
//        mapDesignPane.setAlignment(Pos.CENTER);
        mapDesignController.addMapToPane(mapDesignPane);
        stage.getScene().setRoot(mapDesignPane);
        stage.setTitle("design Map");
        stage.show();
    }

//    public void makeLoginAlert(String result) {
//        VBox popUp = new VBox();
//        Button button = new Button();
//        Pane pane = ((Pane)width.getScene().getRoot());
//        style.popUp0(pane, popUp, button, 80, 50, 400, 295, 400, 100,200, 50, 450, 213, result, 20);
//        popUpTransition(popUp, 0, 1);
//        pane.getChildren().get(0).setDisable(true);
////        disableHBoxes(0.4);
//        button.setOnMouseClicked(mouseEvent -> {
//            popUpTransition(popUp, 1, 0);
//        });
//    }
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
//                disableHBoxes(1);
                ((Pane) popUp.getParent()).getChildren().remove(1);
            });
        }
    }
}
