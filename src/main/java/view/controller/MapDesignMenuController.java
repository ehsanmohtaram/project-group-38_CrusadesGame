package view.controller;

import controller.Controller;
import controller.LoginController;
import controller.MapDesignController;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import view.Style;

import java.util.HashMap;

public class MapDesignMenuController {
//    private final MapDesignController mapDesignController;
    private final Controller controller;
    private final Style style;
    private Stage stage;
    private TextField width;
    private TextField height;
    private TextField name;

    public MapDesignMenuController() {
        controller = new Controller();
//        this.mapDesignController = new MapDesignController();
        this.style = new Style();
    }

    public void getInfoFromMenu(Stage stage, TextField width, TextField height, TextField name){
        this.stage = stage;
        this.width = width;
        this.height = height;
        this.name = name;
    }

    public void createNewMap(){
        HashMap<String, String> optionsMaker = new HashMap<>();
        optionsMaker.put("x", width.getText());
        optionsMaker.put("y", height.getText());
        optionsMaker.put("n", name.getText());
        String result = controller.createNewMap(optionsMaker);
        if(result.equals("successful")) startDesignMap();
        else showError(result);
    }

    private void showError(String result) {

        VBox popUp = new VBox();
        Button button = new Button();
        Pane pane = ((Pane)width.getScene().getRoot());
        style.popUp0(pane, popUp, button, 80, 50, 400, 295, 400, 100,200, 50, 450, 213, result, 20);
//        popUpTransition(popUp, 0, 1);
        pane.getChildren().get(0).setDisable(true);
//        disableHBoxes(0.4);
//        button.setOnMouseClicked(mouseEvent -> {
//            popUpTransition(popUp, 1, 0);
//        });
    }

    private void startDesignMap() {

    }
}
