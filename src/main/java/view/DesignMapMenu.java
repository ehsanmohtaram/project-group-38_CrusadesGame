package view;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Map;
import view.controller.MapDesignMenuController;

import java.util.Objects;

public class DesignMapMenu extends Application {
    private final MapDesignMenuController menuController;
    private Stage stage;
    private Style style;
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
        style.button0(submit, "create", 180 , 70);
        style.button0(back, "back" , 180 , 70);
        style.button0(defaultMaps, "use defaults" , 250 , 70);
        submit.setFont(style.Font0(25));
        back.setFont(style.Font0(25));
        defaultMaps.setFont(style.Font0(25));
        HBox buttons = new HBox(submit, back);
        buttons.setSpacing(40);

        VBox details = new VBox(name, size, buttons, defaultMaps);
        details.setSpacing(30);
        changeAttributes(width, height, size);
        processNameChange(name, changeName, details);
        VBox vBox = new VBox();
        vBox.setLayoutX(500);  vBox.setLayoutY(213);
        vBox.setSpacing(30);
        pane.getChildren().add(vBox);
        menuController.processDefaultMapSelection(stage, vBox, defaultMaps);
        submit.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                startGame(pane);
            }
        });
        back.setOnMouseClicked(mouseEvent -> {
            new MainMenu().start(stage);
            Map.DEFAULT_MAPS.clear();
        });
        details.setAlignment(Pos.CENTER);
        details.setLayoutX(890);  details.setLayoutY(213);

        pane.getChildren().add(details);

    }

    private void startGame(Pane pane) {
        menuController.getInfoFromMenu(stage, finalWidth, finalHeight, finalName);
        menuController.createNewMap();
    }

    private void changeAttributes(Label width, Label height, HBox size) {
        width.setOnMouseEntered(e -> {
            width.requestFocus();
            width.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
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
                }
            });
        });
        width.setOnMouseExited(e -> {
            size.requestFocus();
        });
        height.setOnMouseEntered(e -> {
            height.requestFocus();
            height.setOnKeyPressed(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent keyEvent) {
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
                }
            });
        });
        height.setOnMouseExited(e -> {
            size.requestFocus();
        });

    }

    public void processNameChange(Label name, TextField changeName, VBox details){
        name.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getClickCount() == 2){
                    details.getChildren().remove(name);
                    details.getChildren().add(0, changeName);
                    changeName.requestFocus();
                    changeName.setOnKeyPressed(new EventHandler<KeyEvent>() {
                        @Override
                        public void handle(KeyEvent keyEvent) {
                            if(keyEvent.getCode().getName().equals("Enter")){
                                name.setText(changeName.getText());
                                details.getChildren().remove(changeName);
                                details.getChildren().add(0, name);
                                details.requestFocus();
                            }
                        }
                    });
                }
            }
        });
    }


    public String run(){
//        System.out.println("You can design your map here. When your map is ready input 'start game'");
//        HashMap<String , String> options;
//        String input, result;
//        while (true) {
//            input = CommandParser.getScanner().nextLine();
//            if ((options = commandParser.validate(input,"set texture",
//                    "x|positionX/y|positionY/x1/y1/x2/y2/t|type")) != null)
//                System.out.println(mapDesignController.setTexture(options));
//            else if ((options = commandParser.validate(input,"clear","x|positionX/y|positionY")) != null) {
//                System.out.println(mapDesignController.clear(options));
//            }else if ((options = commandParser.validate(input,"drop rock","x|positionX/y|positionY/d|direction")) != null) {
//                System.out.println(mapDesignController.dropRock(options));
//            }else if ((options = commandParser.validate(input,"drop tree","x|positionX/y|positionY/t|type")) != null) {
//                System.out.println(mapDesignController.dropTree(options));
//            }else if ((options = commandParser.validate(input,"add user","x|positionX/y|positionY/u|user/f|flag")) != null) {
//                System.out.println(mapDesignController.addUserToMap(options));
//            }else if ((options = commandParser.validate(input,"drop building","x|positionX/y|positionY/t|type/f|flag")) != null) {
//                System.out.println(mapDesignController.dropBuilding(options));
//            }else if ((options = commandParser.validate(input,"drop unit","x|positionX/y|positionY/t|type/f|flag/c|count")) != null) {
//                System.out.println(mapDesignController.dropUnit(options));
//            }else if ((options = commandParser.validate(input,"show map","x|positionX/y|positionY")) != null) {
//                result = mapDesignController.showMap(options);
//                System.out.println(result);
//                if(result.matches("map:\\sin[\\S\\s]+")) {
//                    System.out.println("you entered map menu");
//                    return "map";
//                }
//            }else if(commandParser.validate(input,"start game",null) != null) {
//                result = mapDesignController.startPlaying();
//                if (!result.equals("start")) System.out.println(result);
//                else return "start";
//            } else if (commandParser.validate(input,"show current menu",null) != null)
//                System.out.println("design map Menu");
//            else System.out.println("invalid command");
//        }
        return null;
    }


}
