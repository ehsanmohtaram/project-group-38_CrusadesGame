package view;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Map;
import view.controller.MapDesignMenuController;

public class DesignMapMenu extends Application {
    private final MapDesignMenuController menuController;
    private Stage stage;
    private final Style style;
    private Integer finalWidth;
    private Integer finalHeight;
    private String name;
    private Label finalName;

    public DesignMapMenu() {
        menuController = new MapDesignMenuController();
        this.style = new Style();
        finalWidth = 10;
        finalHeight = 10;
        name = "new map";
    }

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setResizable(false);
        Pane pane = new Pane();
        BackgroundSize backgroundSize = new BackgroundSize(1920, 1080, false, false, false, false);
        Image image = new Image(LoginMenu.class.getResource("/images/background/loginBack.jpg").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        pane.setBackground(new Background(backgroundImage));
        createMenu(pane);
        stage.getScene().setRoot(pane);
        stage.setTitle("design Menu");
        stage.show();
    }

    private void createMenu(Pane pane) {
        Label name = new Label(this.name);
        this.finalName = name;
        style.label0(name, 400 , 70);
        name.setFont(style.Font0(35));
        TextField changeName = new TextField();
        style.textFiled0(changeName, null, 400 , 70);
        changeName.setFont(style.Font0(35));
        Label width = new Label(finalWidth.toString());
        Label height = new Label(finalHeight.toString());
        Label cross = new Label("*");
        HBox size = new HBox(width, cross , height);
        size.setAlignment(Pos.CENTER);
        for (Node child : size.getChildren()) {
            ((Label) child).setFont(style.Font0(35));
            style.label0((Label) child , 100 , 70);
        }
        size.setSpacing(50);
        Button submit = new Button();
        Button back = new Button();
        Button defaultMaps = new Button();
        Button myMap = new Button();
        style.button0(submit, "create", 180 , 70);
        style.button0(back, "back" , 180 , 70);
        style.button0(defaultMaps, "use defaults" , 250 , 70);
        style.button0(myMap, "my map" , 250 , 70);
        submit.setFont(style.Font0(25));
        back.setFont(style.Font0(25));
        defaultMaps.setFont(style.Font0(25));
        myMap.setFont(style.Font0(25));
        HBox buttons = new HBox(submit, back);
        buttons.setSpacing(40);

        VBox details = new VBox(name, size, buttons, defaultMaps, myMap);
        details.setSpacing(30);
        changeAttributes(width, height, size);
        processNameChange(name, changeName, details);
        VBox vBox = new VBox();
        vBox.setLayoutX(500);  vBox.setLayoutY(213);
        vBox.setSpacing(30);
        pane.getChildren().add(vBox);
        menuController.processMapSelection(stage, vBox, defaultMaps, myMap);
        submit.setOnMouseClicked(mouseEvent -> startGame());
        back.setOnMouseClicked(mouseEvent -> {
            new MainMenu().start(stage);
            Map.DEFAULT_MAPS.clear();
        });
        details.setAlignment(Pos.CENTER);
        details.setLayoutX(890);  details.setLayoutY(213);

        pane.getChildren().add(details);

    }

    private void startGame() {
        menuController.getInfoFromMenu(stage, finalWidth, finalHeight, finalName);
        menuController.createNewMap();
    }

    private void changeAttributes(Label width, Label height, HBox size) {
        width.setOnMouseEntered(e -> {
            width.requestFocus();
            width.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode().getName().equals("Right")){
                    if(finalWidth < 100) {
                        finalWidth += 5;
                        width.setText(finalWidth.toString());
                    }
                }
                if(keyEvent.getCode().getName().equals("Left")){
                    if(finalWidth > 20) {
                        finalWidth -= 5;
                        width.setText(finalWidth.toString());
                    }
                }
            });
        });
        width.setOnMouseExited(e -> size.requestFocus());
        height.setOnMouseEntered(e -> {
            height.requestFocus();
            height.setOnKeyPressed(keyEvent -> {
                if(keyEvent.getCode().getName().equals("Right")){
                    if(finalHeight < 100) {
                        finalHeight += 5;
                        height.setText(finalHeight.toString());
                    }
                }
                if(keyEvent.getCode().getName().equals("Left")){
                    if(finalHeight > 20) {
                        finalHeight -= 5;
                        height.setText(finalHeight.toString());
                    }
                }
            });
        });
        height.setOnMouseExited(e -> size.requestFocus());

    }

    public void processNameChange(Label name, TextField changeName, VBox details){
        name.setOnMouseClicked(mouseEvent -> {
            if(mouseEvent.getClickCount() == 2){
                details.getChildren().remove(name);
                details.getChildren().add(0, changeName);
                changeName.requestFocus();
                changeName.setOnKeyPressed(keyEvent -> {
                    if(keyEvent.getCode().getName().equals("Enter")){
                        name.setText(changeName.getText());
                        details.getChildren().remove(changeName);
                        details.getChildren().add(0, name);
                        details.requestFocus();
                    }
                });
            }
        });
    }

}
