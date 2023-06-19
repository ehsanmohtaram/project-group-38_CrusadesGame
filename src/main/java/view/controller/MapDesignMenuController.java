package view.controller;

import controller.Controller;
import controller.MapDesignController;
import javafx.animation.ScaleTransition;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;
import model.Map;
import view.DesignMapMenu;
import view.LoginMenu;
import view.MainMenu;
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
    private Pane mapDesignPane;
    private Pane mapPane;

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
        mapDesignPane = new Pane();
        stage.getScene().setRoot(mapDesignPane);
        BackgroundSize backgroundSize = new BackgroundSize(1920, 1080, false, false, false, false);
        Image image = new Image(Objects.requireNonNull(LoginMenu.class.getResource("/images/background/designMenu.jpg")).toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        mapDesignPane.setBackground(new Background(backgroundImage));
        mapPane = mapDesignController.getGameMapPane();
//        mapDesignPane.getChildren().add(mapPane);
        mapDesignController.addMapToPane(mapDesignPane);
//        mapPane.setManaged(false);
        addToolBar();
        cameraProcess();

        mapDesignController.handelMapSelection();
        stage.setTitle("design Map");
        stage.show();
    }

    public void cameraProcess(){
        mapPane.requestFocus();
//        if (mapPane.getLayoutX() + mapPane.getPrefWidth() == 1512) break;
//        else if(mapPane.getLayoutX() + mapPane.getWidth() - 20 < 1512) mapPane.setLayoutX(-mapPane.getPrefWidth() + 1512);
//        else mapPane.setLayoutX(mapPane.getLayoutX() - 20);
        mapPane.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent keyEvent) {
                switch (keyEvent.getCode()){
                    case LEFT: if (mapPane.getLayoutX() + 20 == 0) break;
                    else if (mapPane.getLayoutX() + 20 > 0 ) mapPane.setLayoutX(0);
                    else mapPane.setLayoutX(mapPane.getLayoutX() + 20); break;
                    case DOWN: mapPane.setLayoutY(mapPane.getLayoutY() - 20); break;
                    case RIGHT: mapPane.setLayoutX(mapPane.getLayoutX() - 20); break;
                    case UP: if (mapPane.getLayoutY() + 20 == 0) break;
                    else if (mapPane.getLayoutY() + 20 > 0 ) mapPane.setLayoutY(0);
                    else mapPane.setLayoutY(mapPane.getLayoutY() + 20); break;
                }
            }
        });
    }

    public void addToolBar(){
        Button addUser = new Button("add user");
        Button designMap = new Button("design");
        Button startGame = new Button("start");
        Button back = new Button("back");
        Label error = new Label("");
        error.setFont(style.Font0(10));
        HBox controlButtons = new HBox(addUser, designMap, startGame, back);
        controlButtons.setAlignment(Pos.CENTER);
        controlButtons.setSpacing(10);
        for (Node child : controlButtons.getChildren()) {
            style.button1((Button) child, 150, 50);
            ((Button) child).setFont(style.Font0(25));
        }
        VBox designControls = new VBox(controlButtons, error);
        designControls.setSpacing(5);
        designControls.setAlignment(Pos.CENTER);
        Pane toolBar = new Pane(designControls);
        designControls.setPrefSize(1000, 200);
        BackgroundSize backgroundSize = new BackgroundSize(1000, 200, false, false, false, false);
        Image image = new Image(LoginMenu.class.getResource("/images/menus/toolbar.png").toExternalForm());
        BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER, backgroundSize);
        designControls.setBackground(new Background(backgroundImage));
        toolBar.setLayoutX(300);
        toolBar.setLayoutY(680);
        startGame.setOnMouseClicked(mouseEvent -> startMap(error));
        designMap.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                designControls.getChildren().remove(controlButtons);
                handelDesignMap(designControls);
            }
        });

        addUser.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                designControls.getChildren().remove(controlButtons);
                handelAddUser(designControls, error);
            }
        });
        back.setOnMouseClicked(mouseEvent -> new DesignMapMenu().start(stage));

        mapDesignPane.getChildren().add(toolBar);
    }

    public void startMap(Label error) {
        String result = mapDesignController.startPlaying();
        if (!result.equals("start")) {
            error.setText(result);
            error.setFont(style.Font0(15));
        }
    }

    private void handelAddUser(VBox designControls, Label error) {

        HashMap<String, String> options = new HashMap<>();
        MenuItem flag1 = new MenuItem("red");
        flag1.setGraphic(new Rectangle(30 , 30 , Color.rgb(218, 34 , 34)));
        MenuItem flag2 = new MenuItem("blue");
        flag2.setGraphic(new Rectangle(30 , 30 , Color.rgb(10, 150 , 180)));
        MenuItem flag3 = new MenuItem("yellow");
        flag3.setGraphic(new Rectangle(30 , 30 , Color.rgb(218, 200 , 34)));
        MenuItem flag4 = new MenuItem("white");
        flag4.setGraphic(new Rectangle(30 , 30 , Color.rgb(218, 218 , 218)));
        MenuItem flag5 = new MenuItem("black");
        flag5.setGraphic(new Rectangle(30 , 30 , Color.rgb(0, 0 , 0)));
        MenuItem flag6 = new MenuItem("purple");
        flag6.setGraphic(new Rectangle(30 , 30 , Color.rgb(150, 50 , 200)));
        MenuItem flag7 = new MenuItem("green");
        flag7.setGraphic(new Rectangle(30 , 30 , Color.rgb(30, 200 , 34)));
        MenuItem flag8 = new MenuItem("orange");
        flag8.setGraphic(new Rectangle(30 , 30 , Color.rgb(218, 100 , 34)));
        MenuButton flags = new MenuButton("flags");
        flags.getItems().addAll(flag1, flag2, flag3, flag4, flag5, flag6, flag7, flag8);
        flags.setBorder(new Border(new BorderStroke(Color.rgb(170,139,100,0.8), BorderStrokeStyle.SOLID, new CornerRadii(3), BorderStroke.THIN)));
        flags.setFont(style.Font0(20));
        for (MenuItem item : flags.getItems()) {
            item.setOnAction(e -> {
                options.put("f", item.getText());
                flags.setText(item.getText());
                flags.setFont(style.Font0(20));
            });
        }
        flags.setStyle("-fx-background-color: transparent;");
        TextField userName = new TextField();
        style.textFiled0(userName, "enter username here", 250, 50);
        userName.setFont(style.Font0(18));
        Button submit = new Button();
        style.button0(submit, "add user", 100 , 50);
        submit.setFont(style.Font0(18));
        Button back = new Button();
        style.button0(back, "back", 100 , 50);
        back.setFont(style.Font0(18));
        flags.setAlignment(Pos.CENTER);
        HBox newUserInfo = new HBox(userName, flags, submit, back);
        newUserInfo.setAlignment(Pos.CENTER);
        newUserInfo.setSpacing(10);
        designControls.getChildren().add(newUserInfo);
        submit.setOnMouseClicked(e -> {
            options.put("u" , userName.getText());
            String result = mapDesignController.addUserToMap(options);
            if(!result.equals("successful")) {
                error.setText(result);
            }
            else {
                flags.setText("flags");
                flags.setFont(style.Font0(20));
                userName.setText(""); userName.setFont(style.Font0(18));
            }
        });
        back.setOnMouseClicked(mouseEvent -> {
            addToolBar();
            designControls.getChildren().remove(newUserInfo);
        });
    }

    private void handelDesignMap(VBox designControls) {
        GridPane designCommands = new GridPane();
        designCommands.setAlignment(Pos.CENTER);
        designCommands.setHgap(10);
        designCommands.setVgap(5);
        Button setTexture = new Button("setTexture");
        Button dropRock = new Button("dropRock");
        Button dropTree = new Button("dropTree");
        Button dropUnit = new Button("dropUnit");
        Button dropBuilding = new Button("dropBuilding");
        Button clear = new Button("clear");
        designCommands.add(setTexture, 0 , 0);
        designCommands.add(dropRock, 1  , 0);
        designCommands.add(dropTree, 2 , 0);
        designCommands.add(dropUnit, 0 , 1);
        designCommands.add(dropBuilding,1  ,1 );
        designCommands.add(clear, 2 , 1);
        for (Node child : designCommands.getChildren()) {
            style.button1((Button) child, 150 , 50);
            ((Button) child).setFont(style.Font0(20));
        }
        Button back = new Button("back");
        style.button1(back, 100, 25);
        back.setFont(style.Font0(15));
        designCommands.add(back, 3 , 1);
        back.setAlignment(Pos.BOTTOM_CENTER);
        back.setOnMouseClicked(mouseEvent -> addToolBar());
        designControls.getChildren().add(0,designCommands);
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

    public void processDefaultMapSelection(Stage stage, VBox mapButtons,Button defaultMaps) {
        this.stage = stage;
        defaultMaps.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
//                int[] counter = {1};
                if (mapButtons.getChildren().size() > 0) mapButtons.getChildren().clear();
                else {
                    for (String defaultMap : controller.showDefaultMaps()) {
                        Button map = new Button();
                        style.button0(map, defaultMap, 310, 70);
                        map.setFont(style.Font0(25));
                        mapButtons.getChildren().add(map);
                        map.setOnMouseClicked(new EventHandler<MouseEvent>() {
                            @Override
                            public void handle(MouseEvent mouseEvent) {
                                mapDesignController = controller.selectDefaultMap(controller.showDefaultMaps().indexOf(defaultMap));
                                startDesignMap();
                            }
                        });
//                    counter[0] ++;
                    }
                }
            }
        });
    }
}
